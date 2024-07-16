package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "student commands")
@RequiredArgsConstructor
public class RunCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run test command", key = {"r", "run"})
    public void run() {
        testRunnerService.run();
    }
}
