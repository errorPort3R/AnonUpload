package com.ironyard.entities;

import javax.persistence.*;

/**
 * Created by jeffryporter on 6/27/16.
 */

@Entity
@Table(name="files")
public class AnonFile
{
    @Id
    @GeneratedValue
    int id;

    @Column(nullable=false)
    String originalFilename;

    @Column(nullable=false)
    String realFilename;

    @Column(nullable=false)
    boolean keep;

    String nickname;

    String password;

    public AnonFile()
    {
    }

    public AnonFile(String originalFilename, String realFilename, boolean keep)
    {
        this.originalFilename = originalFilename;
        this.realFilename = realFilename;
        this.keep = keep;

    }

    public AnonFile(int id, String originalFilename, String realFilename)
    {
        this.id = id;
        this.originalFilename = originalFilename;
        this.realFilename = realFilename;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getOriginalFilename()
    {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename)
    {
        this.originalFilename = originalFilename;
    }

    public String getRealFilename()
    {
        return realFilename;
    }

    public void setRealFilename(String realFilename)
    {
        this.realFilename = realFilename;
    }

    public boolean isKeep()
    {
        return keep;
    }

    public void setKeep(boolean keep)
    {
        this.keep = keep;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
