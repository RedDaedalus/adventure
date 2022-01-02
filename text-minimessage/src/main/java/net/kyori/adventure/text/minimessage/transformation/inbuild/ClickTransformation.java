/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A transformation applying a click event.
 *
 * @since 4.10.0
 */
public final class ClickTransformation extends Transformation {
  public static final String CLICK = "click";

  private final ClickEvent.Action action;
  private final String value;

  /**
   * Create a new click event transformation.
   *
   * @param name the tag name
   * @param args tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static ClickTransformation create(final String name, final List<TagPart> args) {
    if (args.size() != 2) {
      throw new ParsingException("Don't know how to turn " + args + " into a click event", args);
    }
    final ClickEvent.@Nullable Action action = ClickEvent.Action.NAMES.value(args.get(0).value().toLowerCase(Locale.ROOT));
    final String value = args.get(1).value();
    if (action == null) {
      throw new ParsingException("Unknown click event action '" + args.get(0).value() + "'", args);
    }

    return new ClickTransformation(action, value);
  }

  private ClickTransformation(final ClickEvent.Action action, final String value) {
    this.action = action;
    this.value = value;
  }

  @Override
  public Component apply() {
    return Component.empty().clickEvent(ClickEvent.clickEvent(this.action, this.value));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final ClickTransformation that = (ClickTransformation) other;
    return this.action == that.action && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.value);
  }
}
