package com.jumbotours.commons.image.thumbnail;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class contains the variables and methods to create thumbnails from pictures.
 * 
 * @author Oscar Alonso, Josep Carbonell
 * 
 */
public class ThumbnailCreation {

	/** The logger. */
	private static final Logger	logger	= Logger.getLogger(ThumbnailCreation.class);

	/** The img file path. */
	private String				imgFilePath;

	/** The sizes. */
	private String				sizes;

	/** The output path. */
	private String				outputPath;

	/**
	 * Instantiates a new thumbnail creation.
	 * 
	 * @param imgFilePath
	 *            the img file path
	 * @param sizes
	 *            the sizes
	 * @param outputPath
	 *            the output path
	 */
	public ThumbnailCreation(String imgFilePath, String sizes, String outputPath) {
		this.imgFilePath = imgFilePath;
		this.sizes = sizes;
		this.outputPath = outputPath;
	}

	/**
	 * Gets the image file path.
	 * 
	 * @return the image file path
	 */
	public String getImgFilePath() {
		return imgFilePath;
	}

	/**
	 * Sets the image file path.
	 * 
	 * @param imgFilePath
	 *            the new image file path
	 */
	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}

	/**
	 * Gets the output path.
	 * 
	 * @return the output path
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * Sets the output path.
	 * 
	 * @param outputPath
	 *            the new output path
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * Gets the sizes.
	 * 
	 * @return the sizes
	 */
	public String getSizes() {
		return sizes;
	}

	/**
	 * Sets the sizes.
	 * 
	 * @param sizes
	 *            the new sizes
	 */
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	/**
	 * Creates the thumbnails.
	 * 
	 * @return the list of created thumbnails
	 */
	public List<Thumbnail> create() {

		List<Thumbnail> thumbnailsCreated = new ArrayList<>();

		String[] sizesList = StringUtils.split(sizes, ",");

		for (String size : sizesList) {
			String widthHeightArray = StringUtils.trim(size);
			String[] widthHeight = widthHeightArray.split("x");
			int thumbWidth = Integer.valueOf(widthHeight[0].trim());
			int thumbHeight = Integer.valueOf(widthHeight[1].trim());
			String imgRootName = imgFilePath.substring(imgFilePath.lastIndexOf(System.getProperty("file.separator")) + 1, imgFilePath.lastIndexOf("."));
			String imgSize = "-" + thumbWidth + "x" + thumbHeight;
			String imgExtension = imgFilePath.substring(imgFilePath.lastIndexOf("."));
			String imageName = imgRootName + imgSize + imgExtension;
			String fileThumbName = outputPath + System.getProperty("file.separator") + imageName;
			// creates dirs
			Image image = new ImageIcon(imgFilePath).getImage();
			int imgWidth = image.getWidth(null);
			int imgHeight = image.getHeight(null);
			boolean isSmallImg = imgWidth < thumbWidth || imgHeight < thumbHeight;
			int xCrop = 0, yCrop = 0, widthCrop = imgWidth, heightCrop = imgHeight;
			if (imgWidth != -1 && imgHeight != -1) {
				if (isSmallImg) {
					StringBuilder builder = new StringBuilder();
					builder.append("The size of the image ");
					builder.append(imgFilePath);
					builder.append(" (");
					builder.append(imgWidth);
					builder.append("x");
					builder.append(imgHeight);
					builder.append(") ");
					builder.append(" is smaller than the size of the desired thumbnail (");
					builder.append(thumbWidth);
					builder.append("x");
					builder.append(thumbHeight);
					builder.append(") so this thumbnail will be scale.");
					logger.warn(builder.toString());
				}

				double desiredRatio = thumbWidth / (double) thumbHeight;
				double realRatio = imgWidth / (double) imgHeight;

				if (realRatio > desiredRatio) {
					widthCrop = (int) (imgHeight * desiredRatio);
					xCrop = (imgWidth - widthCrop) / 2;
				} else {
					heightCrop = (int) (imgWidth / desiredRatio);
					yCrop = (imgHeight - heightCrop) / 2;
				}

				try {
					FilteredImageSource fis = new FilteredImageSource(image.getSource(), new CropImageFilter(xCrop, yCrop, widthCrop, heightCrop));
					image = Toolkit.getDefaultToolkit().createImage(fis);

					MediaTracker mediaTracker = new MediaTracker(new Container());
					mediaTracker.addImage(image, 0);
					mediaTracker.waitForID(0);

					BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics2D = thumbImage.createGraphics();
					graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

					// The image is scaled to the thumbnail size
					graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
					ImageIO.write(thumbImage, StringUtils.substringAfter(imgExtension, "."), new File(fileThumbName));
					logger.debug("Created image: " + fileThumbName);

					Thumbnail thumbnail = new Thumbnail();
					thumbnail.setPath(fileThumbName);
					thumbnail.setWidth(thumbWidth);
					thumbnail.setHeight(thumbHeight);
					thumbnail.setPoorQuality(isSmallImg);
					thumbnailsCreated.add(thumbnail);

				} catch (IOException | InterruptedException e) {
					logger.error("ERROR in createThumbnail method with image " + imgFilePath + " : " + e);
				}
			} else {
				logger.warn("The thumbnail of " + imgFilePath + " was NOT CREATED. The image is not valid.");
			}
		}

		return thumbnailsCreated;
	}

	/**
	 * The Class Thumbnail.
	 */
	public class Thumbnail {

		/** The path. */
		private String	path;

		/** The width. */
		private int		width;

		/** The height. */
		private int		height;

		/** The poorQuality. */
		private boolean	poorQuality;

		/**
		 * Gets the height.
		 * 
		 * @return the height
		 */
		public int getHeight() {
			return height;
		}

		/**
		 * Sets the height.
		 * 
		 * @param height
		 *            the new height
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		/**
		 * Gets the path.
		 * 
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Sets the path.
		 * 
		 * @param path
		 *            the new path
		 */
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 * Gets the width.
		 * 
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Sets the width.
		 * 
		 * @param width
		 *            the new width
		 */
		public void setWidth(int width) {
			this.width = width;
		}

		/**
		 * Checks if is poor quality.
		 * 
		 * @return true, if is poor quality
		 */
		public boolean isPoorQuality() {
			return poorQuality;
		}

		/**
		 * Sets the poor quality.
		 * 
		 * @param poorQuality
		 *            the new poor quality
		 */
		public void setPoorQuality(boolean poorQuality) {
			this.poorQuality = poorQuality;
		}
	}
}
