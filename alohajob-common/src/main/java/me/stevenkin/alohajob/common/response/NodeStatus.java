package me.stevenkin.alohajob.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.stevenkin.alohajob.common.model.SystemMetrics;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeStatus {
    private SystemMetrics systemMetrics;
}
