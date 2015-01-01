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

package me.dags.daflight.input;

import me.dags.daflight.input.binds.BindType;
import me.dags.daflight.input.binds.KeyBind;
import me.dags.daflight.minecraft.MCGame;
import me.dags.daflight.player.DaPlayer;
import me.dags.daflight.player.Direction;
import me.dags.daflight.player.Vector;
import me.dags.daflight.utils.Config;

/**
 * @author dags_ <dags@dags.me>
 */

public class MovementHandler extends MCGame
{

    private static final double sr = 1 / Math.sqrt(2);

    public static void handleMovementInput(DaPlayer daPlayer)
    {
        double yaw = getPlayer().rotationYaw;
        double pitch = getPlayer().rotationPitch;

        Vector movementVector = new Vector();
        Direction direction = daPlayer.direction.update(yaw);

        boolean b1 = false;
        boolean b2 = false;
        double lrMod = Config.getInstance().lrModifier;

        for (KeyBind kb : DaPlayer.KEY_BINDS.movementBinds)
        {
            if (kb.keyHeld())
            {
                /////
                movementVector.setSpeed(daPlayer.getSpeed());
                movementVector.setHasInput(true);

                double x;
                double y;
                double z;

                if (kb.getType().equals(BindType.STRAFE))
                {
                    b1 = true;
                    x = direction.getZ() * kb.getModX() * movementVector.getSpeed() * lrMod;
                    y = 1.15 * kb.getModY() * movementVector.getSpeed();
                    z = direction.getX() * kb.getModZ() * movementVector.getSpeed() * lrMod;

                    movementVector.setHasLateralInput(true);
                }
                else
                {
                    b2 = true;
                    x = direction.getX() * kb.getModX() * movementVector.getSpeed();
                    y = 1.15 * kb.getModY() * movementVector.getSpeed();
                    z = direction.getZ() * kb.getModZ() * movementVector.getSpeed();

                    if (kb.getType().equals(BindType.MOVE))
                    {
                        movementVector.setHasLateralInput(true);
                    }
                    if (daPlayer.cineFlightOn || daPlayer.is3DFlightOn())
                    {
                        y += (0 - kb.getModX()) * movementVector.getSpeed() * (pitch / 90);
                    }
                }
                movementVector.add(x, y, z);
            }
        }
        if (b1 && b2)
        {
            movementVector.multiply(sr);
        }
        daPlayer.direction = direction;
        daPlayer.movementVector = movementVector;
    }
}
