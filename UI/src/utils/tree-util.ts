import { TreeNodeData } from '@arco-design/web-vue/es/tree/interface';
import { TreeOptionData } from '@/types/global';

/**
 * 根据key查找树节点的title
 * @param tree
 * @param targetValue
 */
const findTreeTitleByKey = (
  tree: TreeNodeData[],
  targetValue: string
): string | undefined => {
  function findLabelRecursive(nodes: TreeNodeData[], value: string): string {
    if (!nodes) {
      return '';
    }
    let foundLabel: string | undefined = '';
    nodes.forEach((node) => {
      if (node.key === value) {
        foundLabel = node.title;
      } else if (node.children) {
        const label = findLabelRecursive(node.children, value);
        if (label !== '') {
          foundLabel = label;
        }
      }
    });
    return foundLabel;
  }

  return findLabelRecursive(tree, targetValue);
};

/**
 * 根据key查找从父到子的全路径名称,用/分割
 */
const findFullPathNameByKey = (
  tree: TreeNodeData[],
  targetValue: string
): string | undefined => {
  function findLabelRecursive(nodes: TreeNodeData[], value: string): string {
    if (!nodes) {
      return '';
    }
    let foundLabel: string | undefined = '';
    nodes.forEach((node) => {
      if (node.key === value) {
        foundLabel = node.title;
      } else if (node.children) {
        const label = findLabelRecursive(node.children, value);
        if (label !== '') {
          foundLabel = `${node.title}/${label}`;
        }
      }
    });
    return foundLabel;
  }

  return findLabelRecursive(tree, targetValue);
};

/**
 * 方法findKeyByFullPathName
 * @param tree
 * @param targetValue 从父到子的名称拼接而来,拼接字符串为 /
 * @returns
 */
const findKeyByFullPathName = (
  tree: TreeNodeData[],
  targetValue: string
): string | undefined => {
  // 辅助函数，用于递归查找节点
  function findNode(
    nodes: TreeNodeData[],
    fullPath: string[],
    currentIndex: number
  ): string | undefined {
    // eslint-disable-next-line no-restricted-syntax
    for (const node of nodes) {
      const currentPathPart = fullPath[currentIndex];
      // 检查当前节点的 title 是否与路径的一部分匹配
      if (node.title === currentPathPart) {
        // 如果是最后一个部分，返回 key
        if (currentIndex === fullPath.length - 1) {
          return node.key as string;
        }
        // 否则，递归地在子节点中搜索
        if (node.children) {
          const childKey = findNode(node.children, fullPath, currentIndex + 1);
          if (childKey) {
            return childKey;
          }
        }
      }
    }
    return undefined;
  }

  // 将目标值拆分为路径部分数组
  const fullPath = targetValue.split('/');
  // 从根节点开始搜索
  return findNode(tree, fullPath, 0);
};

/**
 * 用于树搜索过滤
 * @param keyword 搜索关键字
 * @param originTreeData 树数据
 */
const searchTreeData = (keyword: string, originTreeData: TreeNodeData[]) => {
  const loop = (data: TreeNodeData[]) => {
    const result: TreeNodeData[] = [];
    data.forEach((item: TreeNodeData) => {
      if (item.title) {
        if (item.title.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
          result.push({ ...item });
        } else if (item.children) {
          const filterData = loop(item.children);
          if (filterData.length) {
            result.push({
              ...item,
              children: filterData,
            });
          }
        }
      }
    });
    return result;
  };
  return loop(originTreeData);
};

// Helper function to get nested property
function getNestedProperty(obj: any, path: string) {
  return path.split('.').reduce((o, p) => (o ? o[p] : undefined), obj);
}

const filterEmptyTreeNodeData = (
  attrName: string,
  treeNodeData: TreeNodeData[]
) => {
  const loop = (data: TreeNodeData[]) => {
    const result: TreeNodeData[] = [];
    data.forEach((item: any) => {
      if (item.children && item.children.length > 0) {
        const filterData = loop(item.children);
        if (filterData.length) {
          result.push({
            ...item,
            children: filterData,
          });
        } else if (getNestedProperty(item, attrName) > 0) {
          result.push({ ...item, children: [] });
        }
      } else if (getNestedProperty(item, attrName) === 0) {
        // 过滤掉属性值为0的节点
      } else {
        result.push({ ...item });
      }
    });
    return result;
  };
  return loop(treeNodeData);
};

// 根据key过滤掉树节点
const filterTreeNodeDataByKey = (
  nodeKey: string,
  treeNodeData: TreeOptionData[]
) => {
  const loop = (data: TreeNodeData[]) => {
    const result: TreeNodeData[] = [];
    data.forEach((item: any) => {
      if (item.children && item.children.length > 0) {
        const filterData = loop(item.children);
        if (filterData.length && item.value !== nodeKey) {
          result.push({
            ...item,
            children: filterData,
          });
        } else if (item.value !== nodeKey) {
          result.push({ ...item, children: null });
        }
      } else if (item.value !== nodeKey) {
        result.push({ ...item });
      }
    });
    return result;
  };
  return loop(treeNodeData);
};

/**
 * 根据key查找树节点的父节点
 * @param node
 * @param target
 * @param parents
 */
function findParents(tree: TreeNodeData[], target: string): TreeNodeData[] {
  const parents: TreeNodeData[] = [];

  function findNode(
    node: TreeNodeData,
    currentParents: TreeNodeData[]
  ): boolean {
    if (node.key === target) {
      parents.push(...currentParents);
      return true;
    }

    if (node.children) {
      const newParents = [...currentParents, node];
      return node.children.some((child) => findNode(child, newParents));
    }

    return false;
  }

  tree.some((node) => findNode(node, []));
  return parents;
}

function walk(
  currentNode: TreeOptionData | TreeOptionData[],
  callback: (node: TreeOptionData) => void
) {
  if (Array.isArray(currentNode)) {
    currentNode.forEach((child) => walk(child, callback));
  } else {
    callback(currentNode);
    if (currentNode.children) {
      walk(currentNode.children, callback);
    }
  }
}

/**
 * 根据key查找从父到子的全keys
 */
const findAllParentKeyByNodeKey = (
  tree: TreeNodeData[],
  targetValue: string
): string[] => {
  function findParentKeyRecursive(
    nodes: TreeNodeData[],
    value: string
  ): string[] {
    if (!nodes) {
      return [];
    }
    const foundKeys: string[] = [];
    nodes.forEach((node) => {
      if (node.key === value) {
        foundKeys.push(node.key);
      } else if (node.children) {
        const keys = findParentKeyRecursive(node.children, value);
        if (keys.length > 0) {
          foundKeys.push(...keys);
          foundKeys.push(node.key as string);
        }
      }
    });
    return foundKeys;
  }

  return findParentKeyRecursive(tree, targetValue);
};

export {
  findTreeTitleByKey,
  searchTreeData,
  findParents,
  filterEmptyTreeNodeData,
  filterTreeNodeDataByKey,
  walk,
  findFullPathNameByKey,
  findKeyByFullPathName,
  findAllParentKeyByNodeKey,
};
