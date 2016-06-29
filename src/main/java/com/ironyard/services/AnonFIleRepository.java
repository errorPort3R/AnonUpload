package com.ironyard.services;

import com.ironyard.entities.AnonFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;

/**
 * Created by jeffryporter on 6/27/16.
 */

public interface AnonFileRepository extends CrudRepository<AnonFile, Integer>
{
    public Iterable<AnonFile> findByKeep(Boolean keep);
    public Integer countByKeep(boolean keep);
}
