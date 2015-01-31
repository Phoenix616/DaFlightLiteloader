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

package me.dags.daflightliteloader.minecraft.transformers;

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * @author dags_ <dags@dags.me>
 */

public class ObfTable extends Obf
{

    public static ObfTable EntityPlayer = new ObfTable("net.minecraft.entity.player.EntityPlayer", "yz");
    public static ObfTable fall = new ObfTable("func_70069_a", "b", "fall");
    public static ObfTable jump = new ObfTable("func_70664_aZ", "bj", "jump");

    public static ObfTable EntityPlayerSP = new ObfTable("net.minecraft.client.entity.EntityPlayerSP", "blk");
    public static ObfTable getFOVModifier = new ObfTable("func_71151_f", "t", "getFOVMultiplier");

    public static ObfTable EntityRenderer = new ObfTable("net.minecraft.client.renderer.EntityRenderer", "blt");
    public static ObfTable setupViewBobbing = new ObfTable("func_78475_f", "g", "setupViewBobbing");

    public static ObfTable EntityClientPlayerMP = new ObfTable("net.minecraft.client.entity.EntityClientPlayerMP", "bjk");
    public static ObfTable sendMotionUpdates = new ObfTable("func_71166_b", "a", "sendMotionUpdates");

    public ObfTable(String seargeName, String obfName)
    {
        super(seargeName, obfName, seargeName);
    }

    public ObfTable(String seargeName, String obfName, String mcpName)
    {
        super(seargeName, obfName, mcpName);
    }

}