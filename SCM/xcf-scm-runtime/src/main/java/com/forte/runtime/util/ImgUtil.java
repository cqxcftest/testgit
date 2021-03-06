package com.forte.runtime.util;

/**
 * Created by libin on 14-11-12.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

@Deprecated
public class ImgUtil {

    public static BufferedImage fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

    public static File thumb2File(byte[] data,int width,int height,String path) throws Exception {
        BufferedImage img = thumb(fromByteArray(data),width,height,true);
        return img2File(img,path,null);
    }

    public static File buffer2File(byte[] data,String path,String format) throws IOException {
        BufferedImage img=fromByteArray(data);
        return img2File(img,path,format);
    }

    public static File img2File(BufferedImage img,String path,String format) throws IOException {
        File p = new File(path);
        if(!p.getParentFile().exists()){
            p.getParentFile().mkdirs();
        }
        if(!p.exists())
            p.createNewFile();
        if(format==null){
            format = "jpg";
        }
        ImageIO.write(img,format,p);
        return p;
    }

    /**
     * 生成缩略图 <br/>保存:ImageIO.write(BufferedImage, imgType[jpg/png/...], File);
     *
     * @param source
     *            原图片
     * @param width
     *            缩略图宽
     * @param height
     *            缩略图高
     * @param b
     *            是否等比缩放
     * */
    public static BufferedImage thumb(BufferedImage source, int width,
                                      int height, boolean b) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) width / source.getWidth();
        double sy = (double) height / source.getHeight();

        if (b) {
            if (sx > sy) {
                sx = sy;
                width = (int) (sx * source.getWidth());
            } else {
                sy = sx;
                height = (int) (sy * source.getHeight());
            }
        }

        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width,
                    height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else
            target = new BufferedImage(width, height, type);
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 图片水印
     *
     * @param imgPath
     *            待处理图片
     * @param markPath
     *            水印图片
     * @param x
     *            水印位于图片左上角的 x 坐标值
     * @param y
     *            水印位于图片左上角的 y 坐标值
     * @param alpha
     *            水印透明度 0.1f ~ 1.0f
     * */
    public static void waterMark(String imgPath, String markPath, int x, int y,
                                 float alpha) {
        try {
            // 加载待处理图片文件
            Image img = ImageIO.read(new File(imgPath));

            BufferedImage image = new BufferedImage(img.getWidth(null), img
                    .getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(img, 0, 0, null);

            // 加载水印图片文件
            Image src_biao = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, x, y, null);
            g.dispose();

            // 保存处理后的文件
            FileOutputStream out = new FileOutputStream(imgPath);
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字水印
     *
     * @param imgPath
     *            待处理图片
     * @param text
     *            水印文字
     * @param font
     *            水印字体信息
     * @param color
     *            水印字体颜色
     * @param x
     *            水印位于图片左上角的 x 坐标值
     * @param y
     *            水印位于图片左上角的 y 坐标值
     * @param alpha
     *            水印透明度 0.1f ~ 1.0f
     */

    public static void textMark(String imgPath, String text, Font font,
                                Color color, int x, int y, float alpha) {
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;

            Image img = ImageIO.read(new File(imgPath));

            BufferedImage image = new BufferedImage(img.getWidth(null), img
                    .getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawString(text, x, y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(imgPath);
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * http://im4java.sourceforge.net/docs/dev-guide.html
     */
    public static BufferedImage thumbnail(BufferedImage source, int width,int height,
                                          String format,String quality,int type)throws Exception{
        /*IMOperation op = new IMOperation();
        op.addImage();
        op.addRawArgs("-format",format);//"WEBP"
        if(width == 0){//根据高度缩放图片
            op.resize(null, height);
        }else if(height == 0){//根据宽度缩放图片
            op.resize(width, null);
        }else {
            op.resize(width, height);
        }
        *//*String raw = "";
        if(type == 1){
            //按像素
            raw = width+"x"+height+"^";
        }else{
            //按像素百分比
            raw = width+"%x"+height+"%";
        }
        op.addRawArgs("-sample" ,  raw );*//*
        if((quality !=null && !quality.equals(""))){
            op.addRawArgs("-quality" ,  quality );
        }
        //op.thumbnail(width,height,"^");
        op.addImage("-");// output

        ConvertCmd convert = new ConvertCmd(true);
        if(AppContextConfig.get("os.type","linux").equals("windows")) {
            convert.setSearchPath(AppContextConfig.get("graphicsMagic.path", "C:\\Program Files\\GraphicsMagick-1.3.24-Q16"));
        }
        *//*String osName = System.getProperty("os.name").toLowerCase();
        if(osName.indexOf("win") != -1) {
            convert.setSearchPath(AppContextConfig.get("graphicsMagic.path", "C:\\Program Files\\GraphicsMagick-1.3.24-Q16"));
        }*//*
        BufferedImage img = null;
        Stream2BufferedImage buff = new Stream2BufferedImage();
        convert.setOutputConsumer(buff);
        // run command and extract BufferedImage from OutputConsumer
        convert.run(op,source);
        img = buff.getImage();
        return img;*/
        throw new InvalidClassException("not-supported-method");
    }
}
