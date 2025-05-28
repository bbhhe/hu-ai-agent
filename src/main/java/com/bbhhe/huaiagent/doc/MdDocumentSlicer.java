package com.bbhhe.huaiagent.doc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.*;
import java.util.stream.Collectors;

public class MdDocumentSlicer {


    private final Parser markdownParser = Parser.builder().build();

    @Resource
    VectorStore pgVectorVectorStore;

    public static void main(String[] args) {
        MdDocumentSlicer mdDocumentSlicer = new MdDocumentSlicer();
        String s = FileUtil.readString("E:\\workspace\\hu-ai-agent\\src\\main\\resources\\agent\\note4-rag\\笔记4-RAG知识库基础.md", "utf-8");
        List<Map<String, Object>> maps = mdDocumentSlicer.sliceDocument(s);

        ArrayList<Document> collect = maps.stream().map(map -> {
            String text = String.valueOf(map.get("text"));
            String textEmbedding = String.valueOf(map.get("text_for_embedding"));
            map.remove("text_for_embedding");
            map.remove("text");
            return  new Document(textEmbedding, map);
        }).collect(Collectors.toCollection(ArrayList::new));

        System.out.println(JSONUtil.toJsonPrettyStr(maps.get(0)));
    }


    public List<Map<String, Object>> sliceDocument(String markdownContent) {
        List<Map<String, Object>> slices = new ArrayList<>();
        Node document = markdownParser.parse(markdownContent);
        StringBuilder currentContent = new StringBuilder();
        List<String> currentHeadings = new ArrayList<>();
        String currentSection = "";
        int sliceId = 1;
        List<Map<String, String>> currentImages = new ArrayList<>();

        // Traverse Markdown AST
        Node node = document.getFirstChild();
        while (node != null) {
            if (node instanceof Heading) {
                Heading heading = (Heading) node;
                String headingText = getText(heading);
                while (currentHeadings.size() >= heading.getLevel()) {
                    currentHeadings.remove(currentHeadings.size() - 1);
                }
                currentHeadings.add(headingText);
                currentSection = String.join(" > ", currentHeadings);

                // Start new slice if content exists
                if (currentContent.length() > 0) {
                    slices.add(createSlice(sliceId++, currentContent.toString(), currentSection, currentImages));
                    currentContent.setLength(0);
                    currentImages.clear();
                }
            } else if (node instanceof FencedCodeBlock || node instanceof Paragraph || node instanceof BulletList) {
                // Append content to current slice
                String renderedContent = renderNode(node);
                currentContent.append(renderedContent).append("\n");

                // Process images in the content
                processImages(renderedContent, currentImages);
            } else if (node instanceof Image) {
                Image image = (Image) node;
                String alt = getText(image);
                String url = image.getDestination();
                currentImages.add(createImageInfo(alt, url));
                currentContent.append(String.format("<image:%s|%s>", alt, url)).append("\n");
            }

            // Move to next node
            Node next = node.getFirstChild();
            if (next == null) {
                next = node.getNext();
                if (next == null) {
                    Node parent = node.getParent();
                    while (parent != null && next == null) {
                        next = parent.getNext();
                        parent = parent.getParent();
                    }
                }
            }
            node = next;
        }

        // Add final slice
        if (currentContent.length() > 0) {
            slices.add(createSlice(sliceId, currentContent.toString(), currentSection, currentImages));
        }

        return slices;
    }

    private Map<String, String> createImageInfo(String alt, String url) {
        Map<String, String> imageInfo = new HashMap<>();
        imageInfo.put("alt", alt);
        imageInfo.put("url", url);
        return imageInfo;
    }

    private void processImages(String content, List<Map<String, String>> images) {
        // 处理markdown格式的图片 ![alt](url)
        String pattern = "!\\[(.*?)\\]\\((.*?)\\)";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(content);

        while (m.find()) {
            String alt = m.group(1);
            String url = m.group(2);
            images.add(createImageInfo(alt, url));
        }
    }

    private Map<String, Object> createSlice(int id, String content, String sectionPath, List<Map<String, String>> images) {
        Map<String, Object> slice = new HashMap<>();
        slice.put("id", "slice_" + String.format("%03d", id));
        slice.put("file_source", "笔记4-RAG知识库基础.md");
        slice.put("chapter_path", sectionPath);
        slice.put("content_type", detectContentType(content));
        slice.put("content", content);

        // 添加原始文本和用于嵌入的文本
        String textForEmbedding = removeImagePlaceholders(content);
        slice.put("text", content);
        slice.put("text_for_embedding", textForEmbedding);

        // 添加图片信息
        slice.put("images", images);

        // Extract keywords using Spring AI
        List<String> keywords = extractKeywords(textForEmbedding);
        slice.put("keywords", keywords);

        return slice;
    }

    private String removeImagePlaceholders(String content) {
        // 移除图片占位符
        return content.replaceAll("<image:.*?\\|.*?>", "");
    }

    private String getText(Node node) {
        StringBuilder text = new StringBuilder();
        Node child = node.getFirstChild();
        while (child != null) {
            if (child instanceof Text) {
                text.append(((Text) child).getLiteral());
            }
            child = child.getNext();
        }
        return text.toString();
    }

    private String renderNode(Node node) {
        if (node instanceof FencedCodeBlock) {
            FencedCodeBlock codeBlock = (FencedCodeBlock) node;
            return "```" + codeBlock.getInfo() + "\n" + codeBlock.getLiteral() + "```\n";
        } else if (node instanceof Paragraph) {
            return getText(node) + "\n";
        } else if (node instanceof BulletList) {
            StringBuilder listContent = new StringBuilder();
            Node item = node.getFirstChild();
            while (item != null) {
                listContent.append("- ").append(getText(item)).append("\n");
                item = item.getNext();
            }
            return listContent.toString();
        }
        return "";
    }

    private String detectContentType(String content) {
        if (content.contains("```xml") || content.contains("```yml") || content.contains("```yaml")) {
            return "配置文件";
        } else if (content.contains("```java")) {
            return "代码";
        } else if (content.contains("Trae Builder")) {
            return "指令";
        }
        return "描述";
    }

    private List<String> extractKeywords(String content) {
        // Use Spring AI EmbeddingClient to extract keywords (simplified)
        // In practice, use a proper NLP model or embedding-based keyword extraction
        String[] words = content.toLowerCase().split("\\s+");
        Set<String> keywords = new HashSet<>();
        List<String> commonKeywords = Arrays.asList(
                "spring", "boot", "jdk21", "mybatis", "plus", "knife4j", "trae",
                "controller", "service", "mapper", "entity", "blog", "thumb", "user"
        );

        for (String word : words) {
            if (commonKeywords.contains(word)) {
                keywords.add(word);
            }
        }

        // Optionally, use embeddingClient for advanced keyword extraction
        // float[] embeddings = embeddingClient.embed(content);
        // (Process embeddings to extract top keywords)

        return new ArrayList<>(keywords);
    }
}