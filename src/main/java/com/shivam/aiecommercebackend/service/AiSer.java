package com.shivam.aiecommercebackend.service;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AiSer {

    @Autowired
    private ChatClient client;

//    This is without the rag flow
    @PreAuthorize("denyAll()")
    public String getDescription(String q){
        String prompt = """
        Rewrite the following query for clarity and completeness,
        then generate a product description in 100 words based on the rewritten query:
        %s
        """.formatted(q);

        String response = client.prompt()
                .system("You are an expert product description writer.")
                .user(prompt)
                .call()
                .content();

        return response;
    }


//    This is using the rag flow
    @PreAuthorize("denyAll()")
    public String getDescriptionRag(String q){
//        Here i am only using the Pre-Retrieval phase because we are not retrieving anything from the db
        var advisor= RetrievalAugmentationAdvisor.builder()
                .queryTransformers(
                        RewriteQueryTransformer.builder().chatClientBuilder(client.mutate().clone()).build(),
                        TranslationQueryTransformer.builder().chatClientBuilder(client.mutate().clone()).targetLanguage("English").build()
                )
                .build();

        String response=client.prompt()
                .advisors(advisor)
                .system("You an expert description provider for the products provide only for 100 words")
                .user(q)
                .call()
                .content();
        return  response;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String rewriteDescription(String productName, String categoryName, String currentDescription) {
        if(productName==null || productName.isBlank() )throw new InvalidInputException("Product name is not valid...");
        if(categoryName==null || categoryName.isBlank())throw new InvalidInputException("Category name is not valid....");
        if(currentDescription==null || categoryName.isBlank())throw new InvalidInputException("Current Description is not valid");

        String prompt = """
        You are an expert product description writer.

        Product Name: %s
        Category: %s
        Current Description: %s

        Rewrite the description to make it:
        1. Clear, professional, and engaging.
        2. Focused on the product’s main features.
        3. Short enough to not exceed 500 characters (strictly follow this limit).

        Return only the rewritten description, do not add anything else.
        """.formatted(productName, categoryName, currentDescription);

        String rewritten = client.prompt()
                .system("You are a professional copywriter.")
                .user(prompt)
                .call()
                .content();

        return rewritten;
    }
}
