package cn.emac.demo.petstore.common;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * 分页查询结果，同时提供Lambda风格的接口。
 * <pre>
 *     Pagination<Integer> src = new Pagination<>(Lists.newArrayList(1, 2, 3));
 *     Pagination<String> dest = src.stream().map(String::valueOf).collect(Pagination.toPagination());
 * </pre>
 *
 * @author Emac
 * @since 2017-05-25
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
    public Pagination(@NotNull List<T> content) {
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
    public static <T> Collector<T, ?, Pagination<T>> toPagination() {
        return Collector.of(Pagination::new, (page, elm) -> page.getContent().add(elm),
                (left, right) -> {
                    left.getContent().addAll(right.getContent());
                    return left;
                });
    }
}
