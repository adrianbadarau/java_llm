package com.adrianbadarau.langchainjava.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.BertTokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static dev.langchain4j.data.document.FileSystemDocumentLoader.loadDocument;

@Configuration
public class LLMConfig {
    @Bean
    EmbeddingModel embeddingModel(){
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) throws IOException {
        // I'm going to use the in memory store for now but in the future we should switch to a vector DB
        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
        Resource resource = resourceLoader.getResource("classpath:t&c.text");
        Document document = loadDocument(resource.getFile().toPath());
        DocumentSplitter splitter = DocumentSplitters.recursive(100, 0, new BertTokenizer());
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();
        ingestor.ingest(document);

        return store;
    }
}
