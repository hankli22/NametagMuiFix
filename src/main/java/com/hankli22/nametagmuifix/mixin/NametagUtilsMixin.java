package com.hankli22.nametagmuifix.mixin;

import meteordevelopment.meteorclient.utils.render.NametagUtils;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Meteor's NametagUtils expects Window.calculateScale(1, false) to be 1.
 *
 * Vanilla returns 1 for that call, but Modern UI replaces the GUI-scale
 * calculation and clamps the result to its own minimum (2+). Meteor then
 * divides the projected framebuffer coordinates by that wrong scale,
 * detaching nametags from entities.
 *
 * onRender() is the only place that writes NametagUtils.windowScale.
 * Restoring it to the vanilla value keeps the rest of Meteor's coordinate
 * pipeline untouched.
 */
@Mixin(NametagUtils.class)
public abstract class NametagUtilsMixin {
    @Shadow
    private static double windowScale;

    @Inject(method = "onRender", at = @At("RETURN"))
    private static void nametagMuiFix$restoreVanillaWindowScale(Matrix4f modelView, CallbackInfo ci) {
        windowScale = 1.0D;
    }
}
