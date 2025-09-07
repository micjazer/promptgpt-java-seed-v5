
package com.codeiguanas.promptgpt.web.repo;
import com.codeiguanas.promptgpt.domain.*; import com.fasterxml.jackson.databind.ObjectMapper; import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.*; import org.springframework.core.io.support.PathMatchingResourcePatternResolver; import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct; import java.io.*; import java.util.*; import java.util.concurrent.ConcurrentHashMap; import java.util.stream.Collectors;
@Repository public class FileTemplateRepository {
    private final Map<String,PromptTemplate> byId=new ConcurrentHashMap<>(); private final ObjectMapper yaml=new ObjectMapper(new YAMLFactory());
    @PostConstruct public void loadAll() throws IOException {
        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath*:prompts/*.yaml");
        for (Resource r: res){ try(InputStream in=r.getInputStream()){ SeedTemplateDTO dto=yaml.readValue(in, SeedTemplateDTO.class); PromptTemplate t=map(dto); byId.put(t.id(),t);} }
    }
    public List<PromptTemplate> list(Optional<String> q, Optional<String> domain, Optional<String> tag){
        return byId.values().stream()
            .filter(t -> q.map(s -> (t.name()+" "+t.description()+" "+String.join(" ", t.tags())).toLowerCase().contains(s.toLowerCase())).orElse(true))
            .filter(t -> domain.map(d -> t.tags().contains("domain:"+d)).orElse(true))
            .filter(t -> tag.map(tg -> t.tags().contains(tg)).orElse(true))
            .sorted(Comparator.comparing(PromptTemplate::name)).collect(Collectors.toList());
    }
    public Optional<PromptTemplate> get(String id){ return Optional.ofNullable(byId.get(id)); }
    private PromptTemplate map(SeedTemplateDTO dto){
        List<TemplateVariable> vars=new ArrayList<>(); if(dto.variables!=null) for(var v: dto.variables){
            TemplateVariable.Type type=TemplateVariable.Type.STRING; try{ type=TemplateVariable.Type.valueOf(v.type==null?"STRING":v.type.toUpperCase()); }catch(Exception ignored){}
            vars.add(new TemplateVariable(v.name,type,v.required,v.defaultValue,v.description)); }
        Set<String> tags=dto.tags==null?new HashSet<>():new HashSet<>(dto.tags);
        if(dto.domain!=null&&!dto.domain.isBlank()) tags.add("domain:"+dto.domain);
        if(dto.role!=null&&!dto.role.isBlank()) tags.add("role:"+dto.role);
        return new PromptTemplate(dto.id, dto.name==null?dto.id:dto.name, dto.description==null?"":dto.description,
                dto.language==null?"fr":dto.language, dto.audience==null?"":dto.audience, dto.tone==null?"":dto.tone,
                dto.format==null?"":dto.format, tags, vars, dto.content, "seeds"); }
}
