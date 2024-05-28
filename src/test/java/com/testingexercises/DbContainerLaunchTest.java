package com.testingexercises;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DbContainerLaunchTest extends DatabaseContainerTest {

    @Test
    void shouldStart() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }
}