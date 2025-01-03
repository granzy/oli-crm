package cn.oli.crm.model.vo.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;


/**
 * 级联选择值对象
 *
 * @author gzy
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CascadeSelectVo<T> {

    private String id;
    private String pid;
    private String value;
    private String label;
    private Boolean isLeaf = false;
    private T data;
    private List<? extends CascadeSelectVo<T>> children;

    public CascadeSelectVo(String id) {
        this.id = id;
    }

    /**
     * 根据id递归查询节点
     *
     * @param node node
     * @return Optional<Tree < T>>
     */
    public Optional<CascadeSelectVo<T>> findNodeById(CascadeSelectVo<T> node) {
        Optional<CascadeSelectVo<T>> result = Optional.empty();
        if (node.getId().equals(this.getId())) {
            result = Optional.of(this);
        } else {
            if (!children.isEmpty()) {
                for (CascadeSelectVo<T> child : children) {
                    if (node.getId().equals(child.getId())) {
                        result = Optional.of(child);
                        break;
                    } else {
                        result = child.findNodeById(node);
                        if (result.isPresent()) {
                            break;
                        }
                    }
                }
            } else {
                result = Optional.empty();
            }
        }
        return result;
    }

    /**
     * 根据id查询子树
     *
     * @param ids ids
     * @return Set<String>
     */
    public Set<String> getSubTreeId(Set<String> ids) {
        Set<String> result = new HashSet<>();
        for (String id : ids) {
            CascadeSelectVo<T> node = new CascadeSelectVo<>(id);
            Optional<CascadeSelectVo<T>> optionalTree = findNodeById(node);
            if (optionalTree.isPresent()) {
                List<CascadeSelectVo<T>> flatTree = flatTree(optionalTree.get());
                for (CascadeSelectVo<T> flatNode : flatTree) {
                    result.add(flatNode.getId());
                }
            }
        }
        return result;
    }

    /**
     * 扁平化树
     *
     * @param root root
     * @return List<Tree < T>>
     */
    private List<CascadeSelectVo<T>> flatTree(CascadeSelectVo<T> root) {
        List<CascadeSelectVo<T>> result = new ArrayList<>();
        result.add(root);
        for (CascadeSelectVo<T> node : root.getChildren()) {
            result.add(node);
            result.addAll(this.flatTree(node));
        }
        return result;
    }
}
