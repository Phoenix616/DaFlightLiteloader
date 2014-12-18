/*
 * Copyright (c) 2014, dags_ <dags@dags.me>
 *
 *  Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 *  granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING
 *  ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL,
 *  DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 *  WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE
 *  USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package me.dags.daflight.minecraft.uielements;

import me.dags.daflight.minecraft.Colour;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class GuiEntryBox extends GuiTextField
{
    private boolean coloured = true;
    private boolean isActive;

    public GuiEntryBox(FontRenderer fr, int x, int y, int width, int height, boolean colour)
    {
        super(fr, x, y, width, height);
        this.setFocused(false);
        this.coloured = colour;
    }

    public boolean isActive()
    {
        return this.isActive;
    }

    public void setString(String s)
    {
        if (coloured)
        {
            s = Colour.addColour(s);
        }
        this.setText(s);
    }

    public void draw()
    {
        this.drawTextBox();
    }

    public void setActive()
    {
        this.setText(Colour.stripColour(getText()));
        this.setFocused(coloured);
        this.setTextColor(0xFF5555);
        this.isActive = true;
    }

    public void unsetActive()
    {
        this.isActive = false;
        this.setFocused(false);
        this.setTextColor(0xFFFFFF);
        this.setString(getText());
        this.setCursorPositionZero();
    }

    public void entry(char keyChar, int id)
    {
        if (id == Keyboard.KEY_BACK && this.getText().length() == 0)
        {
            return;
        }
        super.textboxKeyTyped(keyChar, id);
    }
}