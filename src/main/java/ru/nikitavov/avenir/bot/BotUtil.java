package ru.nikitavov.avenir.bot;

public class BotUtil {
    private long lastExecutionTime = 0;

    public void executeLimitedRequest(int limitMs, Runnable execute) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastExecution = currentTime - lastExecutionTime;

        if (timeSinceLastExecution < limitMs) {
            // Вычисляем оставшееся время ожидания
            long remainingWaitTime = limitMs - timeSinceLastExecution;

            // Ожидаем оставшееся время
            try {
                Thread.sleep(remainingWaitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        execute.run();

        lastExecutionTime = System.currentTimeMillis();
    }
}
