package com.ironyard.controllers;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.ironyard.entities.AnonFile;
import com.ironyard.services.AnonFileRepository;
import com.ironyard.utilities.PasswordStorage;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jeffryporter on 6/27/16.
 */
@Controller
public class AnonFileController
{
    private static final int MAX_OVER_FILE_COUNT = 2;

    @Autowired
    AnonFileRepository files;

    @PostConstruct
    public void init() throws SQLException
    {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String nickname, String password, String keep) throws IOException, PasswordStorage.CannotPerformOperationException
    {
        int loseable=0;
        boolean keeper;
        if (keep != null && keep.equals("on"))
        {
            keeper = true;
        }
        else
        {
            keeper = false;
        }

        File dir = new File("public/files");
        dir.mkdirs();

        File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(uploadedFile);

        fos.write(file.getBytes());

        //get count of keep items
        loseable = files.countByKeep(false);

        //add or reassign files...
        Iterable<AnonFile> workingList = new ArrayList<>();
        workingList = files.findAll();
        if (loseable >= MAX_OVER_FILE_COUNT && !keeper)
        {
            //get oldest item that can be changed
            int identity=0;
            boolean firstRun = true;
            for (AnonFile f : workingList)
            {
                if (firstRun && (!f.isKeep()))
                {
                    identity = f.getId();
                    firstRun = false;
                }
                if((!firstRun && f.getId() < identity) && (!f.isKeep()))
                {
                    identity=f.getId();
                }
            }


            //set new file in database

            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), keeper);
            if (nickname != null || !nickname.equals(""))
            {
                anonFile.setNickname(nickname);
            }

            anonFile.setPassword(PasswordStorage.createHash(password));
            files.save(anonFile);

            String filename = files.findOne(identity).getRealFilename();
            files.delete(identity);
            File delFile = new File("public/files/", filename);
            delFile.delete();
        }
        else
        {
            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName(), keeper);
            if (nickname != null || !nickname.equals(""))
            {
                anonFile.setNickname(nickname);
            }

            anonFile.setPassword(PasswordStorage.createHash(password));
            files.save(anonFile);
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(String id, String password) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException
    {
        int newId = Integer.valueOf(id);
        boolean deleteHash = PasswordStorage.verifyPassword(password, files.findOne(newId).getPassword());
        if (deleteHash)
        {
            String filename = files.findOne(newId).getRealFilename();
            files.delete(newId);
            File delFile = new File("public/files/", filename);
            delFile.delete();

        }
        else
        {
            System.err.printf("Password Incorrect!");
        }

        return "redirect:/";
    }
}
