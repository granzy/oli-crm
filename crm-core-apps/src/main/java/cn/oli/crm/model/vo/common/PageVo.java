package cn.oli.crm.model.vo.common;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * 分页数据
 * TODO: 这个要和Pageable转换
 *
 * @author gzy
 */
public class PageVo {

    public final static int DEFAULT_PAGE_NO = 0;
    public final static int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前记录起始索引
     */
    private Integer current = DEFAULT_PAGE_NO;

    /**
     * 每页显示记录数
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private Sort.Direction orderDirection = Sort.Direction.DESC;

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return orderByColumn + " " + orderDirection;
    }

    public Integer getCurrent() {
        return current;
    }

    public Integer getPageNo() {
        return this.current == null || this.current <= 0 ? DEFAULT_PAGE_NO : this.current - 1;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public Sort.Direction getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(Sort.Direction orderDirection) {
        this.orderDirection = orderDirection;
    }
}
