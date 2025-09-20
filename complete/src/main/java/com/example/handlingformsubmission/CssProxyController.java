package com.example.handlingformsubmission;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

@Controller
public class CssProxyController {
    private static final String CSS_BASE_PATH = "D:/HTML";

    @GetMapping(value = "/**/{filename:.+\\.css}", produces = "text/css; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> serveCss(HttpServletRequest request) throws IOException {
        String contextPath = request.getContextPath();
        String requestPath = request.getRequestURI().substring(contextPath.length());
        // requestPath ví dụ: /myapp/include/style.css hoặc /myapp/member/style.css
        if (!requestPath.endsWith(".css")) {
            return ResponseEntity.notFound().build();
        }
        String cssPath = CSS_BASE_PATH + requestPath.replaceFirst("^/myapp", "");
        Path path = Paths.get(cssPath);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        // Giữ nguyên loại dấu nháy khi thêm context-path
        content = content.replaceAll("url\\((['\"])/(?!/)", "url($1" + contextPath + "/");
        content = content.replaceAll("url\\(\\s*/(?!/)", "url(" + contextPath + "/");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/css; charset=UTF-8")).body(content);
    }
}
