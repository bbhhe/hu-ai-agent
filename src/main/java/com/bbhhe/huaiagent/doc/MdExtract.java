package com.bbhhe.huaiagent.doc;

import cn.hutool.core.io.FileUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * md文档得抽取
 */
public class MdExtract implements DocumentReader {
    private String mdContent;
    private MdDocumentSlicer documentSlicer;
    public MdExtract(String filePath) {
        mdContent =  FileUtil.readString(filePath, "utf-8");

        this.documentSlicer = new MdDocumentSlicer();
    }

    @Override
    public List<Document> get() {
        List<Map<String, Object>> maps = documentSlicer.sliceDocument(mdContent);
        ArrayList<Document> collect = maps.stream().map(map -> {
            String text = String.valueOf(map.get("text"));
            String textEmbedding = String.valueOf(map.get("text_for_embedding"));
            map.remove("text_for_embedding");
            map.remove("text");
            return  new Document(textEmbedding, map);
        }).collect(Collectors.toCollection(ArrayList::new));
        return collect;
    }
}
