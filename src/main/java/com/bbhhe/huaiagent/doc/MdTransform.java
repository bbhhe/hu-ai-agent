package com.bbhhe.huaiagent.doc;

import cn.hutool.core.util.StrUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.List;

public class MdTransform implements DocumentTransformer {

    @Override
    public List<Document> apply(List<Document> documents) {
        for (int i = 0; i < documents.size(); i++) {
            if(StrUtil.isBlank(documents.get(i).getText())){
                documents.set(i,new Document((String) documents.get(i).getMetadata().get("chapter_path"),documents.get(i).getMetadata()));
            }
        }
        return documents;
    }
}
