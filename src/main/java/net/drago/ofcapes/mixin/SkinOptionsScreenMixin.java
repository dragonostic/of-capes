package net.drago.ofcapes.mixin;

import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;

import java.math.BigInteger;
import java.util.Random;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends GameOptionsScreen {

    protected SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, new TranslatableText("options.skinCustomisation.title"));
    }


    //@Inject(method = "init()V", at = @At("TAIL"))
    @Override
    public void init() {
        int i = 0;
        PlayerModelPart[] var2 = PlayerModelPart.values();
        int var3 = var2.length;

        for (PlayerModelPart playerModelPart : var2) {
            addDrawableChild(new ButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, this.getPlayerModelPartDisplayString(playerModelPart), (buttonWidget) -> {
                gameOptions.togglePlayerModelPart(playerModelPart, !gameOptions.isPlayerModelPartEnabled(playerModelPart));
                buttonWidget.setMessage(this.getPlayerModelPartDisplayString(playerModelPart));
            }));
            ++i;
        }

        this.addDrawableChild(Option.MAIN_HAND.createButton(this.gameOptions, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150));
        ++i;

        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, new TranslatableText("options.ofcapes.editor"), (button) -> {
            final Random r1 = new Random();
            final Random r2 = new Random(System.identityHashCode(new Object()));
            final BigInteger random1Bi = new BigInteger(128, r1);
            final BigInteger random2Bi = new BigInteger(128, r2);
            final BigInteger serverBi = random1Bi.xor(random2Bi);
            final String serverId = serverBi.toString(16);
            try {
                if (this.client != null) {
                    this.client.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
                }
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }

            String uRL = String.format("https://optifine.net/capeChange?u=%s&n=%s&s=%s", this.client.getSession().getUuid(), this.client.getSession().getUsername(), serverId);

            Util.getOperatingSystem().open(uRL);
        }));
        ++i;

        if (i % 2 == 1) {
            ++i;
        }

        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
            if (this.client == null) return;
            this.client.openScreen(parent);
        }));
    }

    private Text getPlayerModelPartDisplayString(PlayerModelPart part) {
        return ScreenTexts.composeToggleText(part.getOptionName(), gameOptions.isPlayerModelPartEnabled(part));
    }

}
