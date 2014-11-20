/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
package com.github.daytron.flipit.map.creator;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import org.loadui.testfx.GuiTest;

import static org.loadui.testfx.controls.Commons.hasText;
*/
/**
 *
 * @author ryan
 
@Category(TestFX.class)
public class SampleTest extends GuiTest {

  public static final int ThreeSeconds = 3;
  private final ScheduledThreadPoolExecutor sch
      = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);


  private final Runnable oneShotTask = () -> {
    System.out.println("\t oneShotTask Execution Time: "+ LocalDateTime.now());
    final Button button = find("#btn");
    Platform.runLater(() -> button.setText("was clicked"));
  };

  @Override
  protected Parent getRootNode() {
    final Button btn = new Button();
    btn.setId("btn");
    btn.setText("Hello World");
    btn.setOnAction((actionEvent) -> {
      System.out.println("Submission Time: " + LocalDateTime.now());
      ScheduledFuture<?> oneShotFuture = sch.schedule(oneShotTask, ThreeSeconds, TimeUnit.SECONDS);
    });
    return btn;
  }


  @Test(expected = RuntimeException.class)
  public void shouldClickButton01() {
    final Button button = find("#btn");
    click(button);
    waitUntil(button, hasText("was clicked"), 1);
  }

  @Test
  public void shouldClickButton02() {
    final Button button = find("#btn");
    click(button);
    waitUntil(button, hasText("was clicked"), 10);
  }


}*/