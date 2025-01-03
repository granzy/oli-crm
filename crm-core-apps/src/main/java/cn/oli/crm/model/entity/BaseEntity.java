package cn.oli.crm.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * 实体基础类
 * Entities do not must need to be Serializable.
 * However, if you want to serialize them, you can safely implement Serializable.
 * The identifier has to be Serializable because that’s a JPA requirement since the identifier might be used as the key for a second-level cache entry.
 *
 * @author csj
 */
@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    public static final String TIME_ZONE = "GMT+8";

    /**
     * 主键uuid
     */
    @Id
    @GenericGenerator(name = "pk-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "pk-uuid")
    @Column(length = 64)
    private String id;

    /**
     * 创建时间
     */
    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TIME_ZONE)
    private Instant createTime;

    /**
     * 最后修改时间
     */
    @Column
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TIME_ZONE)
    private Instant updateTime;

}
