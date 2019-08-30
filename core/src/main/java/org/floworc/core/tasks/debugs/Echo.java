package org.floworc.core.tasks.debugs;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.floworc.core.tasks.RunnableTask;
import org.floworc.core.tasks.Task;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Echo extends Task implements RunnableTask {
    private String format;

    @Override
    public Void run() throws Exception {
        log.info(this.format);
        return null;
    }
}