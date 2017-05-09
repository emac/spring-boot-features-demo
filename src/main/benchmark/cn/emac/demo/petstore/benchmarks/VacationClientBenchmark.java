package cn.emac.demo.petstore.benchmarks;

import cn.emac.demo.petstore.clients.VacationClient;
import cn.emac.demo.petstore.clients.VacationClientConfig;
import cn.emac.demo.petstore.common.utils.Asserts;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;

import static cn.emac.demo.petstore.domain.vacation.VacationType.PERSONAL;
import static java.time.DayOfWeek.SUNDAY;

/**
 * @author Emac
 * @since 2017-05-07
 */
@BenchmarkMode(Mode.Throughput)
@Fork(1)
@Threads(Threads.MAX)
@State(Scope.Benchmark)
@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 3, time = 3)
public class VacationClientBenchmark {

    private VacationClient vacationClient;

    @Setup
    public void setUp() {
        VacationClientConfig clientConfig = new VacationClientConfig("http://localhost:3000");
        vacationClient = new VacationClient(clientConfig);
    }

    @Benchmark
    public void benchmarkIsWeekend() {
        VacationRequest request = new VacationRequest();
        request.setType(PERSONAL);
        OffsetDateTime lastSunday = OffsetDateTime.now().with(TemporalAdjusters.previous(SUNDAY));
        request.setStart(lastSunday);
        request.setEnd(lastSunday.plusDays(1));

        Asserts.isTrue(vacationClient.isWeekend(request).isSuccess());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(VacationClientBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
