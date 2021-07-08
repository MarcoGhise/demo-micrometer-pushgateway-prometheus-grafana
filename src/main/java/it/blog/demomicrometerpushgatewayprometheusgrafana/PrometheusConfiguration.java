package it.blog.demomicrometerpushgatewayprometheusgrafana;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PrometheusConfiguration {

	private Map<String, String> groupingKey = new HashMap<>();
	private PushGateway pushGateway;
	private CollectorRegistry collectorRegistry;

	@Value("${prometheus.job.name}")
	private String prometheusJobName;

	@Value("${prometheus.grouping.key}")
	private String prometheusGroupingKey;

	@Value("${prometheus.pushgateway.url}")
	private String prometheusPushGatewayUrl;

	Gauge alive;

	@PostConstruct
	public void init() {

		pushGateway = new PushGateway(prometheusPushGatewayUrl);
		groupingKey.put(prometheusGroupingKey, prometheusJobName);
		PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
		collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();

		alive = Gauge.build().name(prometheusJobName + "_" + prometheusGroupingKey + "_alive").help("Alive signal sent")
				.register(collectorRegistry);

		Metrics.globalRegistry.add(prometheusMeterRegistry);
	}

	public void pushMetrics() {

		try {
			pushGateway.pushAdd(collectorRegistry, prometheusJobName, groupingKey);
		} catch (Throwable ex) {
			log.error("Unable to push metrics to Prometheus Push Gateway", ex);
		}

	}

	public void setAlive() {
		alive.setToCurrentTime();
	}

}
