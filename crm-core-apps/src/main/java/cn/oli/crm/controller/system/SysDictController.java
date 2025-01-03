package cn.oli.crm.controller.system;

import cn.oli.crm.model.entity.system.SysDict;
import cn.oli.crm.model.vo.system.SysDictQueryVo;
import cn.oli.crm.service.system.SysDictService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author gzy
 */
@Validated
@RestController
@RequestMapping("/sysDict")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    @PostMapping
    public String save(@Valid @RequestBody SysDict vO) {
        return sysDictService.save(vO).getId();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") String id) {
        sysDictService.realDeleteById(id);
    }

    @GetMapping("/{id}")
    public SysDict getById(@Valid @NotNull @PathVariable("id") String id) {
        return sysDictService.findById(id).orElse(null);
    }

    @PostMapping("/pageList")
    public Page<SysDict> query(@RequestBody @Valid SysDictQueryVo query) {
        return sysDictService.pageList(query);
    }

    @PostMapping("/findSysDictByTypeAndCode")
    public SysDict findSysDictByTypeAndCode(@RequestBody SysDictQueryVo queryVo) {
        return sysDictService.findSysDictByTypeAndCode(queryVo.getType(), queryVo.getCode());
    }

    @GetMapping("/findSysDictsByType/{type}")
    public List<SysDict> findSysDictsByType(@Valid @NotNull @PathVariable("type") String type) {
        return sysDictService.findSysDictsByType(type);
    }
}
