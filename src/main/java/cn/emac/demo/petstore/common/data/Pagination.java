package cn.emac.demo.petstore.common.data;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * 分页查询结果，同时提供Lambda风格的接口。
 * <pre>
 *     Pagination<Integer> src = new Pagination<>(Lists.newArrayList(1, 2, 3));
 *     Pagination<String> dest = src.stream().map(String::valueOf).collect(Pagination.toPagination(src.getPage(), src.getSize(), src.getTotal()));
 * </pre>
 *
 * @author Emac
 * @since 2018-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {

    // 查询记录集
    private List<T> content = Lists.newArrayList();

    // 当前页索引
    private Integer page;

    // 当前页容量
    private Integer size;

    // 总记录数
    private Optional<Integer> total;

    /**
     * 构造单页结果(content, 0, content.size(), Optional.of(content.size()))
     */
    public Pagination(List<T> content) {
        this.content = content;
        this.page = 0;
        this.size = content.size();
        this.total = Optional.of(content.size());
    }

    public Stream<T> stream() {
        return content.stream();
    }

    public Stream<T> parallelStream() {
        return content.parallelStream();
    }

    /**
     * 实现Stream->Pagination的{@code Collector}。
     */
    public static <T> Collector<T, ?, Pagination<T>> toPagination(Integer page, Integer size, Optional<Integer> total) {
        return Collector.of(() -> {
                    Pagination<T> pagination = new Pagination<>();
                    pagination.setPage(page);
                    pagination.setSize(size);
                    pagination.setTotal(total);
                    return pagination;
                }, (pagination, elm) -> pagination.getContent().add(elm),
                (left, right) -> {
                    left.getContent().addAll(right.getContent());
                    return left;
                });
    }
}
