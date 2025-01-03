package cn.oli.crm.model.vo.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;


/**
 * @author gzy
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TreeVo<T> {

    private String id;
    private String pid;
    private String key;
    private String title;
//    private Boolean selectable;
//    private Boolean disabled = false;
    private Boolean disableCheckbox = false;
//    private Boolean checkable;
//    private Boolean draggable;
    private Boolean isLeaf = false;
    private T data;
    private List<TreeVo<T>> children = new ArrayList<>();

    public TreeVo(String id) {
        this.id = id;
    }

    /**
     * 根据id递归查询节点
     *
     * @param node node
     * @return Optional<Tree < T>>
     */
    public Optional<TreeVo<T>> findNodeById(TreeVo<T> node) {
        Optional<TreeVo<T>> result = Optional.empty();
        if (node.getId().equals(this.getId())) {
            result = Optional.of(this);
        } else {
            if (!children.isEmpty()) {
                for (TreeVo<T> child : children) {
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
            TreeVo<T> node = new TreeVo<>(id);
            Optional<TreeVo<T>> optionalTree = findNodeById(node);
            if (optionalTree.isPresent()) {
                List<TreeVo<T>> flatTree = flatTree(optionalTree.get());
                for (TreeVo<T> flatNode : flatTree) {
                    result.add(flatNode.getId());
                }
            }
        }
        return result;
    }

    public Set<TreeVo<T>> getSubTree(Set<String> ids) {
        Set<TreeVo<T>> result = new HashSet<>();
        for (String id : ids) {
            TreeVo<T> node = new TreeVo<>(id);
            Optional<TreeVo<T>> optionalTree = findNodeById(node);
            if (optionalTree.isPresent()) {
                List<TreeVo<T>> flatTree = flatTree(optionalTree.get());
                result.addAll(flatTree);
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
    private List<TreeVo<T>> flatTree(TreeVo<T> root) {
        List<TreeVo<T>> result = new ArrayList<>();
        result.add(root);
        for (TreeVo<T> node : root.getChildren()) {
            result.add(node);
            result.addAll(this.flatTree(node));
        }
        return result;
    }
}
