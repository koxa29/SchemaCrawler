package sf.util;


import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public final class StopWatch
{

  private static final class TaskInfo
  {

    private final String taskName;
    private final Duration duration;

    TaskInfo(final String taskName, final Duration duration)
    {
      this.taskName = taskName;
      this.duration = duration;
    }

    public Duration getDuration()
    {
      return duration;
    }

    @Override
    public String toString()
    {
      final LocalTime durationLocal = LocalTime.ofNanoOfDay(duration.toNanos());
      return String.format("%s - \"%s\"", durationLocal.format(df), taskName);
    }

  }

  private static final DateTimeFormatter df = new DateTimeFormatterBuilder()
    .appendValue(HOUR_OF_DAY, 2).appendLiteral(':')
    .appendValue(MINUTE_OF_HOUR, 2).appendLiteral(':')
    .appendValue(SECOND_OF_MINUTE, 2).appendFraction(NANO_OF_SECOND, 3, 3, true)
    .toFormatter();

  private final String id;
  private final List<TaskInfo> tasks = new LinkedList<TaskInfo>();

  // State for current task
  private Instant start;
  private boolean running;
  private Duration totalDuration;
  private String currentTaskName;

  public StopWatch(final String id)
  {
    this.id = id;
    totalDuration = Duration.ofNanos(0);
  }

  public String getId()
  {
    return id;
  }

  /**
   * Return whether the stop watch is currently running.
   *
   * @see #currentTaskName()
   */
  public boolean isRunning()
  {
    return running;
  }

  /**
   * Return the number of tasks timed.
   */
  public int size()
  {
    return tasks.size();
  }

  public void start(final String taskName)
  {
    if (running)
    {
      throw new IllegalStateException(String
        .format("Cannot stop \"%s\", since it is already running", id));
    }

    running = true;
    currentTaskName = taskName;
    start = Instant.now();
  }

  public void stop()
  {
    if (!running)
    {
      throw new IllegalStateException(String
        .format("Cannot stop \"%s\", since it is not running", id));
    }

    final Instant stop = Instant.now();
    final Duration runTime = Duration.between(start, stop);

    totalDuration = totalDuration.plus(runTime);

    final TaskInfo lastTaskInfo = new TaskInfo(currentTaskName, runTime);
    tasks.add(lastTaskInfo);

    running = false;
    currentTaskName = null;
    start = null;
  }

  public <V> V time(final String taskName, final Callable<V> callable)
    throws Exception
  {
    start(taskName);
    final V returnValue = callable.call();
    stop();
    return returnValue;
  }

  @Override
  public String toString()
  {
    final StringBuilder buffer = new StringBuilder(1024);

    final LocalTime totalDurationLocal = LocalTime
      .ofNanoOfDay(totalDuration.toNanos());
    buffer.append(String.format("Total time taken for \"%s\" - %s hours%n",
                                id,
                                totalDurationLocal.format(df)));

    for (final TaskInfo task: tasks)
    {
      buffer.append(String
        .format("- %4.1f%% - %s%n",
                calculatePercentage(task.getDuration(), totalDuration),
                task));
    }

    return buffer.toString();
  }

  private double calculatePercentage(final Duration duration,
                                     final Duration totalDuration)
  {
    final long totalSeconds = totalDuration.getSeconds();
    if (totalSeconds == 0)
    {
      return 0;
    }
    else
    {
      return duration.getSeconds() * 100D / totalSeconds;
    }
  }

}
