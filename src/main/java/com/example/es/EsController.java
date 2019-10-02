package com.example.es;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class EsController
{
    private EsDao esDao;

    @Autowired
    public EsController(EsDao esDao) 
    {
        this.esDao = esDao;
    }

    @GetMapping("/all")
    public String searchAll()
    {
        List<Document> documents = esDao.searchAll();
        String result = "";

        for(Document doc : documents)
        {
            result += "title: ";
            result += doc.getC_title();
            result += '\n'; 
        }
        return result;
    }
}