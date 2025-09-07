
package com.codeiguanas.promptgpt.web.controller;
import com.codeiguanas.promptgpt.web.repo.FileTemplateRepository; import org.springframework.http.MediaType; import org.springframework.web.bind.annotation.*;
import java.util.*; import java.util.stream.Collectors;
@RestController @RequestMapping(path="/api/v1", produces=MediaType.APPLICATION_JSON_VALUE)
public class CatalogController {
    private final FileTemplateRepository repo;
    public CatalogController(FileTemplateRepository repo){ this.repo=repo; }
    @GetMapping("/domains") public Set<String> domains(){ return repo.list(Optional.empty(),Optional.empty(),Optional.empty()).stream().flatMap(t->t.tags().stream()).filter(s->s.startsWith("domain:")).map(s->s.substring(7)).collect(Collectors.toCollection(java.util.TreeSet::new)); }
    @GetMapping("/roles") public Set<String> roles(){ return repo.list(Optional.empty(),Optional.empty(),Optional.empty()).stream().flatMap(t->t.tags().stream()).filter(s->s.startsWith("role:")).map(s->s.substring(5)).collect(Collectors.toCollection(java.util.TreeSet::new)); }
    @GetMapping("/tags") public Set<String> tags(){ return repo.list(Optional.empty(),Optional.empty(),Optional.empty()).stream().flatMap(t->t.tags().stream()).filter(s->!s.startsWith("domain:")&&!s.startsWith("role:")).collect(Collectors.toCollection(java.util.TreeSet::new)); }
}
