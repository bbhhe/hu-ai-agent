package com.bbhhe.huaiagent.doc;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MdETLHandler {

    private static final Logger log = LoggerFactory.getLogger(MdETLHandler.class);
    @Resource
    VectorStore pgVectorVectorStore;

//    @PostConstruct
//    public void one(){
//        handler();
//    }

    public void handler(){
        //抽取，从md中
        MdExtract mdExtract = new MdExtract("E:\\workspace\\hu-ai-agent\\src\\main\\resources\\agent\\note4-rag\\笔记4-RAG知识库基础.md");

        //转换
        MdTransform mdTransform = new MdTransform();
        List<Document> documents = mdTransform.apply(mdExtract.read());

        //加载
        pgVectorVectorStore.add(documents);
        log.info("加载完整");
    }
}
