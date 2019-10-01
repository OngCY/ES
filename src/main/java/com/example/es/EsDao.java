package com.example.es;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EsDao
{
    private RestHighLevelClient restHighLevelClient;
    private ObjectMapper objectMapper;

    @Autowired
    public EsDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) 
    {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }
    
    public List<Document> searchAll()
    {
        SearchRequest searchRequest = new SearchRequest("csit_main_report_pink"); 
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
        SearchResponse searchResponse = new SearchResponse();
        
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
        searchRequest.source(searchSourceBuilder); 
        
        try
        {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }
        catch (java.io.IOException e)
        {
            e.getLocalizedMessage();
        }

        return getSearchResult(searchResponse);
    }

    private List<Document> getSearchResult(SearchResponse response) 
    {
        SearchHit[] searchHit = response.getHits().getHits();
        List<Document> resultList = new ArrayList<>();

        for (SearchHit hit : searchHit)
        {
            Document document = new Document();
            document.setC_docid(hit.field("c_docid").getValue().toString());
            document.setC_title(hit.field("c_title").getValue().toString());
            document.setC_body_content(hit.field("c_body_content").getValue().toString());

            resultList.add(document);
        }

        return resultList;
    }
}