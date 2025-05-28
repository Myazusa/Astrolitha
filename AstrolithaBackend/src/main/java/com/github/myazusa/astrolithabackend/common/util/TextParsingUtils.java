package com.github.myazusa.astrolithabackend.common.util;

import com.github.myazusa.astrolithabackend.exception.UnknownException;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.TextCollectingVisitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class TextParsingUtils {
    private final static Tika tika = new Tika();
    private final static AutoDetectParser parser = new AutoDetectParser();
    private final static TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
    private final static ParseContext parseContext = new ParseContext();
    private static AtomicBoolean ocrConfigInited = new AtomicBoolean(false);

    /**
     * 解析文件
     * @param path 文件路径，一般是在./uploads/rag
     * @return 返回纯文本
     */
    public static String ParsingAll(String path){
        Path filePath = Paths.get(path);
        initOCRConfigIfNecessary();

        if (!Files.exists(filePath)) {
            log.error("文件不存在: {}", filePath);
            throw new UnknownException("文件不存在");
        }

        try (InputStream input = Files.newInputStream(filePath)) {
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();

            parser.parse(input, handler, metadata, parseContext);
            return handler.toString();

        } catch (TikaException | SAXException | IOException e) {
            log.error("解析文件时发生错误: {}", e.getMessage(), e);
            throw new UnknownException("解析文件时发生错误");
        }
    }

    private static void initOCRConfigIfNecessary() {
        if (ocrConfigInited.compareAndSet(false, true)) {
            ocrConfig.setLanguage("chi_sim"); // 简体中文识别
            parseContext.set(TesseractOCRConfig.class, ocrConfig);
        }
    }

    @Deprecated
    public static String ParsingPDF(String path) throws IOException {
        PDDocument document = Loader.loadPDF(new File(path));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }
    @Deprecated
    public static String ParsingDocx(String path) throws IOException {
        XWPFDocument docx = new XWPFDocument(new FileInputStream(path));
        List<XWPFParagraph> paragraphs = docx.getParagraphs();
        StringBuilder text = new StringBuilder();
        for (XWPFParagraph para : paragraphs) {
            text.append(para.getText());
        }
        return text.toString();
    }
    @Deprecated
    public static String ParsingDoc(String path) throws IOException {
        HWPFDocument doc = new HWPFDocument(new FileInputStream(path));
        Range range = doc.getRange();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < range.numParagraphs(); i++) {
            text.append(range.getParagraph(i).text());
        }
        return text.toString();
    }
    @Deprecated
    public static String ParsingMarkdown(String path) throws IOException {
        return new TextCollectingVisitor().collectAndGetText(Parser.builder().build().parse(Files.readString(Path.of(path))));
    }
}
