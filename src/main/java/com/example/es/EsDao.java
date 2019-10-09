package com.example.es;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.MoreLikeThisQuery;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class EsDao
{
    private RestHighLevelClient restHighLevelClient;
    private ObjectMapper objectMapper;
    
    @Value("${elasticsearch.index}")
    private String index;

    @Autowired
    public EsDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) 
    {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public List<Document> moreLikeThis(String[] fields, String[] likeThese)
    {
        SearchRequest searchRequest = new SearchRequest(index); 
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
        SearchResponse searchResponse = new SearchResponse();

        searchSourceBuilder.query(QueryBuilders.moreLikeThisQuery(fields, likeThese, null).minTermFreq(1).minDocFreq(1));
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
    
    public List<Document> searchAll()
    {
        SearchRequest searchRequest = new SearchRequest(index); 
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
            document.setC_docid(hit.getSourceAsMap().get("c_docid").toString());
            document.setC_title(hit.getSourceAsMap().get("c_title").toString());
            //document.setC_body_content(hit.getSourceAsMap().get("all_attachment_content").toString());

            resultList.add(document);
        }

        return resultList;
    }
}