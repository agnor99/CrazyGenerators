package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.container.GeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.sync.PacketRequestSync;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class GeneratorScreen<SpecContainer extends GeneratorContainer> extends ContainerScreen<SpecContainer> {
    protected ResourceLocation BACKGROUND_TEXTURE;
    Point RELATIVE_SCREEN_POSITION;
    boolean startSynced = false;
    private String generatorName;


    int oldXSize;

    public GeneratorScreen(SpecContainer screenContainer, PlayerInventory playerInventory, ITextComponent title, String generatorName) {
        super(screenContainer, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 184;
        this.generatorName = generatorName;
        oldXSize = xSize;
    }

    @Override
    protected void init() {
        super.init();
        RELATIVE_SCREEN_POSITION = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);
    }

    protected void setBackgroundTexture(ResourceLocation loc) {
        BACKGROUND_TEXTURE = loc;
    }
    @Override
    public void render(int mouseX, int mouseY, final float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if(!startSynced)requestSync();
        startSynced = true;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int DEFAULT_COLOR = 4210752;

        font.drawString(this.title.getFormattedText(),8.0f, 6.0f, DEFAULT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        blit(RELATIVE_SCREEN_POSITION.x, RELATIVE_SCREEN_POSITION.y, 0 , 0, oldXSize, ySize);

        drawEnergy();

    }
    private void drawEnergy() {
        Point energyContainerPoint = new Point(152,37);
        Point energyContainerTexturePoint = new Point(180,37);
        Dimension energyContainerSize = new Dimension(16,56);

        int currentEnergyContainerHeight = calcHeight(energyContainerSize.height,container.getEnergy(), container.getCapacity());
        int missingHeight = energyContainerSize.height-currentEnergyContainerHeight;

        energyContainerPoint.translate(0,missingHeight);
        energyContainerTexturePoint.translate(0,missingHeight);
        energyContainerSize.setSize(energyContainerSize.width, energyContainerSize.height-missingHeight);

        drawPartRelativeOnScreen(energyContainerPoint, energyContainerTexturePoint, energyContainerSize);
    }
    protected int calcHeight(int maxHeight, int value, int maxValue) {
        return (int)(maxHeight*(((float)value)/((float)maxValue)));
    }


    void drawHoverMessages(Point mousePosition) {
        xSize = oldXSize;
        Point relativeMousePosition = new Point(mousePosition);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawEnergyHover(relativeMousePosition);
        drawInfoHover(relativeMousePosition);
    }

    private void drawInfoHover(Point relativeMousePosition) {
        Point drawPosition = new Point(137,15);
        Dimension textureSize = new Dimension(12,12);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            TranslationTextComponent information = new TranslationTextComponent(generatorName);
            drawHoverMessage(relativeMousePosition, information.getFormattedText());
        }
    }

    private void drawEnergyHover(Point relativeMousePosition) {
        Point drawPosition = new Point(151,36);
        Dimension textureSize = new Dimension(18,58);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            drawHoverMessage(relativeMousePosition, container.getEnergy() + "RF");
        }
    }

    private void drawHoverMessage(Point relativeMousePosition, String message) {
        List<String> lines = breakStringIntoLineList(message,92);
        Dimension boxDimension;

        if(lines.size() == 1) {
            boxDimension = new Dimension(font.getStringWidth(lines.get(0))+9,10*lines.size()+7);
        }else {
            boxDimension = new Dimension(100,10*lines.size()+7);
        }
        xSize = relativeMousePosition.x+boxDimension.width - 10;
        drawHoverBox(relativeMousePosition, boxDimension);
        final int WHITE = 16777215;

        for(int i = 0; i <lines.size(); i++) {
            String line = lines.get(i);

            font.drawString(line, relativeMousePosition.x + RELATIVE_SCREEN_POSITION.x + 5, relativeMousePosition.y + RELATIVE_SCREEN_POSITION.y + 5 + 10*i, WHITE);
        }
    }
    protected List<String> breakStringIntoLineList(String message, int lineWidth) {
        String[] words = message.split(" ");
        List<String> lines = new ArrayList<>();
        lines.add("");
        for(String word: words) {
            String currLine = lines.get(lines.size()-1);
            currLine = currLine + " " + word;
            currLine = currLine.trim();

            if(font.getStringWidth(currLine) < lineWidth) {
                lines.set(lines.size()-1, currLine);
            }else {
                lines.add(word);
            }
        }
        return lines;
    }

    protected void drawHoverBox(Point relativeMousePosition, Dimension boxSize){

        drawBoxTop(relativeMousePosition, boxSize.width);
        Point offsettedPoint = new Point(relativeMousePosition);
        offsettedPoint.translate(0,4);
        drawBoxMid(offsettedPoint, boxSize);
        offsettedPoint.translate(0,boxSize.height-8);

        drawBoxBottom(offsettedPoint,boxSize.width);

    }
    void drawBoxTop(Point relativeMousePosition, int width) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,0);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,0);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,0);
        messagePartDimension[2] = new Dimension(4,4);

        drawPartRelativeOnScreen(relativeMousePosition, messagePartPoint[0], messagePartDimension[0]);
        int xoff = 0;
        for(; xoff < width-8; xoff++) {
            drawPartRelativeOnScreen(new Point(relativeMousePosition.x+xoff+4, relativeMousePosition.y), messagePartPoint[1], messagePartDimension[1]);
        }
        drawPartRelativeOnScreen(new Point(relativeMousePosition.x+xoff+4,relativeMousePosition.y),messagePartPoint[2], messagePartDimension[2]);

    }
    void drawBoxMid(Point offsettedMousePosition, Dimension size) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,5);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,5);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,5);
        messagePartDimension[2] = new Dimension(4,4);


        for(int top = 0; top < size.height-9; top++) {
            Point linePoint = new Point(offsettedMousePosition);
            linePoint.translate(0,top);
            drawPartRelativeOnScreen(linePoint, messagePartPoint[0], messagePartDimension[0]);
            int xoff = 0;
            for(; xoff < size.width-8; xoff++) {
                drawPartRelativeOnScreen(new Point(linePoint.x+xoff+4, linePoint.y), messagePartPoint[1], messagePartDimension[1]);
            }

            linePoint.translate(xoff+4,0);
            drawPartRelativeOnScreen(linePoint, messagePartPoint[2], messagePartDimension[2]);
        }
    }
    void drawBoxBottom(Point offsettedMousePosition, int width) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,7);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,7);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,7);
        messagePartDimension[2] = new Dimension(4,4);

        drawPartRelativeOnScreen(offsettedMousePosition, messagePartPoint[0], messagePartDimension[0]);
        int xoff = 0;
        for(; xoff < width-8; xoff++) {
            drawPartRelativeOnScreen(new Point(offsettedMousePosition.x+xoff+4, offsettedMousePosition.y), messagePartPoint[1], messagePartDimension[1]);
        }
        drawPartRelativeOnScreen(new Point(offsettedMousePosition.x+xoff+4,offsettedMousePosition.y),messagePartPoint[2], messagePartDimension[2]);

    }
    private boolean isMouseOverHoverArea(Point relativeMouseLocation, Point upperPosition, Dimension size) {
        return new Rectangle(upperPosition,size).contains(relativeMouseLocation);
    }

    protected void drawPartRelativeOnScreen(Point positionRelativeToScreen, Point positionTexture, Dimension size) {
        blit(RELATIVE_SCREEN_POSITION.x+positionRelativeToScreen.x, RELATIVE_SCREEN_POSITION.y+positionRelativeToScreen.y, positionTexture.x, positionTexture.y, size.width, size.height);
    }

    private void requestSync() {
        NetworkUtil.INSTANCE.sendToServer(new PacketRequestSync(minecraft.player.dimension, container.getTileEntity().getPos()));
    }

}
