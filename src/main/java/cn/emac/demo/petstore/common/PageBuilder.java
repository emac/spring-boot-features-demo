package cn.emac.demo.petstore.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Emac
 * @since 2016-03-21
 */
@Getter
@RequiredArgsConstructor
public class PageBuilder<T> {

    @NonNull
    private List<T> content;
    @NonNull
    private Pageable pageable;
    @NonNull
    private String sql;
    @NonNull
    private DSLContext dsl;
    @Setter
    private long total;

    /**
     * 创建{@code Page}实例
     *
     * @return
     */
    public Page<T> build() {
        return new PageImpl<T>(content, pageable, total);
    }
}
