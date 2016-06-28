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
    private static final int MAX_OVER_FILE_COUNT = 5;
    private static final int LARGEST_ID = 2000000000;

    @Autowired
    AnonFileRepository files;

    @PostConstruct
    public void init() throws SQLException
    {
        Server.createWebServer().start();
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String nickname, String password) throws IOException, PasswordStorage.CannotPerformOperationException
    {
        int keepCount=0;
        File dir = new File("public/files");
        dir.mkdirs();

        File uploadedFile = File.createTempFile("file", file.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(uploadedFile);

        fos.write(file.getBytes());

        //get count of keep items
        Iterable<AnonFile> workingList = new ArrayList<>();
        workingList = files.findAll();
        for (AnonFile f : workingList)
        {
            if (f.isKeep())
            {
                keepCount++;
            }
        }

        //add or reassign files...
        if ((files.count() >= MAX_OVER_FILE_COUNT) && (files.count()<=(keepCount+MAX_OVER_FILE_COUNT)))
        {
            int identity=LARGEST_ID;
            for (AnonFile f : workingList)
            {
                if((f.getId() < identity) && (!f.isKeep()))
                {
                    identity=f.getId();
                }
            }
            if (identity != LARGEST_ID)
            {
                AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName());
                if (nickname != null || !nickname.equals(""))
                {
                    anonFile.setNickname(nickname);
                }
                if (password != null || !password.equals(""))
                {
                    anonFile.setPassword(PasswordStorage.createHash(password));
                }
                files.save(anonFile);
                files.delete(identity);
            }
            else
            {
                System.err.printf("Array Full and ID not available!");
            }
        }
        else
        {
            AnonFile anonFile = new AnonFile(file.getOriginalFilename(), uploadedFile.getName());
            if (nickname != null || !nickname.equals(""))
            {
                anonFile.setNickname(nickname);
            }
            if (password != null || !password.equals(""))
            {
                anonFile.setPassword(PasswordStorage.createHash(password));
            }
            files.save(anonFile);
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(String id)
    {
        int newId = Integer.valueOf(id);
        files.findOne(newId).setKeep(!files.findOne(newId).isKeep());
        return "redirect:/";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String delete(int id, String delete) throws PasswordStorage.CannotPerformOperationException
    {
        //int newId = Integer.valueOf(id);
        if (PasswordStorage.createHash(delete).equals(files.findOne(id).getPassword()))
        {
            files.delete(id);
        }
        else
        {
            System.err.printf("Password Incorrect!");
        }

        return "redirect:/";
    }
}
