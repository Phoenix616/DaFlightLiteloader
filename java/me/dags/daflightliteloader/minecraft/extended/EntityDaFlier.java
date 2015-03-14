package me.dags.daflightliteloader.minecraft.extended;

import me.dags.daflight.DaFlight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Session;
import net.minecraft.world.World;

/**
 * @author dags_ <dags@dags.me>
 */

public class EntityDaFlier extends EntityClientPlayerMP
{
    private DaFlightMovement movementInput;
    private int ticksSinceMovePacket = 0;
    private boolean wasSneaking = false;
    private double oldPosX;
    private double oldMinY;
    private double oldPosZ;
    private double oldRotationYaw;
    private double oldRotationPitch;

    public EntityDaFlier(Minecraft mc, World world, Session session, NetHandlerPlayClient netHandlerPlayClient, StatFileWriter fileWriter)
    {
        super(mc, world, session, netHandlerPlayClient, fileWriter);
        this.movementInput = new DaFlightMovement();

    }

    @Override
    public void onLivingUpdate()
    {
        if (super.movementInput != this.movementInput)
            super.movementInput = this.movementInput;
        this.movementInput.block = flyModOn();
        super.onLivingUpdate();
    }

    @Override
    public void sendMotionUpdates()
    {
        if (DaFlight.get().daPlayer.softFallOn())
        {
            boolean sneaking = super.isSneaking();
            if (sneaking != wasSneaking)
            {
                if (sneaking)
                {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 1));
                }
                else
                {
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 2));
                }
                wasSneaking = sneaking;
            }
            double xChange = this.posX - oldPosX;
            double yChange = this.boundingBox.minY - oldMinY;
            double zChange = this.posZ - oldPosZ;
            double rotationChange = this.rotationYaw - oldRotationYaw;
            double pitchChange = this.rotationPitch - oldRotationPitch;
            boolean sendMovementUpdate = xChange * xChange + yChange * yChange + zChange * zChange > 9.0E-4D || ticksSinceMovePacket >= 20;
            boolean sendLookUpdate = rotationChange != 0.0D || pitchChange != 0.0D;
            if (sendMovementUpdate && sendLookUpdate)
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, true));
            }
            else if (sendMovementUpdate)
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, true));
            }
            else if (sendLookUpdate)
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, true));
            }
            else
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
            ++ticksSinceMovePacket;
            if (sendMovementUpdate)
            {
                oldPosX = this.posX;
                oldMinY = this.boundingBox.minY;
                oldPosZ = this.posZ;
                ticksSinceMovePacket = 0;
            }
            if (sendLookUpdate)
            {
                oldRotationPitch = this.rotationPitch;
                oldRotationYaw = this.rotationYaw;
            }
        }
        else
        {
            super.sendMotionUpdates();
        }
    }

    @Override
    public void moveEntityWithHeading(float f1, float f2)
    {
        super.moveEntityWithHeading(f1, f2);
        if (this.isOnLadder() && !flyModOn() && sprintModOn())
        {
            if (this.isCollidedHorizontally)
            {
                if (this.rotationPitch < -30F)
                {
                    double speed = DaFlight.get().daPlayer.getSpeed();
                    this.moveEntity(0D, speed, 0D);
                }
            }
            else if (!isSneaking() && this.rotationPitch > 40F)
            {
                double speed = DaFlight.get().daPlayer.getSpeed();
                this.moveEntity(0D, -speed, 0D);
            }
        }
    }

    @Override
    public void fall(float distance)
    {
        if (DaFlight.get().daPlayer.softFallOn())
            return;
        super.fall(distance);
    }

    @Override
    public float getFOVMultiplier()
    {
        if (!DaFlight.getConfig().disabled)
            if (flyModOn())
            {
                if (!this.capabilities.isFlying)
                {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
                return 1.0F;
            }
        return super.getFOVMultiplier();
    }

    @Override
    public boolean isOnLadder()
    {
        return !flyModOn() && super.isOnLadder();
    }

    @Override
    public void jump()
    {
        if (DaFlight.get().daPlayer.sprintModOn && !this.capabilities.isFlying)
            this.motionY = 0.42F + 1.25 * DaFlight.getConfig().jumpModifier * DaFlight.get().daPlayer.getSpeed();
        else
            super.jump();
    }

    @Override
    public float getBreakSpeed(Block b, boolean boo)
    {
        float f = super.getBreakSpeed(b, boo);
        if (flyModOn() && (!this.onGround || (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this))))
            f *= 5.0F;
        return f;
    }

    @Override
    public boolean isSneaking()
    {
        return (flyModOn() && movementInput.wasSneaking) || super.isSneaking();
    }

    public boolean flyModOn()
    {
        return DaFlight.get().daPlayer.flyModOn;
    }

    public boolean sprintModOn()
    {
        return DaFlight.get().daPlayer.sprintModOn;
    }
}
