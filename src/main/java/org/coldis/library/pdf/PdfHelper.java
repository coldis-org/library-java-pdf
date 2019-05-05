package org.coldis.library.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * PDF helper.
 */
public class PdfHelper {

	/**
	 * Reads the text from a PDF.
	 *
	 * @param  pdfReader               The PDF reader.
	 * @param  strategy                Strategy to read the text from the PDF.
	 * @param  preventDuplicateContent If duplicate content should be ignored.
	 * @return                         The text from the PDF.
	 * @throws IntegrationException    If there is a problem reading the PDF.
	 */
	public static String readPdfText(final PdfReader pdfReader, final TextExtractionStrategy strategy,
			final Boolean preventDuplicateContent) throws IntegrationException {
		// PDF pages content.
		final List<String> pdfPagesText = new ArrayList<>();
		// Tries to get the PDF text.
		try {
			// For each PDF page.
			for (Integer currentPdfPage = 1; currentPdfPage <= pdfReader.getNumberOfPages(); currentPdfPage++) {
				// Gets the page text.
				final String pdfPageText = PdfTextExtractor.getTextFromPage(pdfReader, currentPdfPage, strategy);
				// If the page text repeats the last page.
				if (preventDuplicateContent && !pdfPagesText.isEmpty()
						&& pdfPageText.startsWith(pdfPagesText.get(pdfPagesText.size() - 1))) {
					// Removes the last added page text.
					pdfPagesText.remove(pdfPagesText.size() - 1);
				}
				// Adds the current page text information to the pages text.
				pdfPagesText.add(pdfPageText);
			}
		}
		// If the text cannot be retrieved.
		catch (final Exception exception) {
			// Throws an PDF read exception.
			throw new IntegrationException(new SimpleMessage("pdf.read.error"), exception);
		}
		// PDF text.
		final StringBuffer pdfText = new StringBuffer();
		// For each PDF page text.
		for (final String pdfPageText : pdfPagesText) {
			// Appends the current PDF page text to the complete PDF text.
			pdfText.append(pdfPageText);
		}
		// Returns the PDF text.
		return pdfText.toString();
	}

	/**
	 * Reads the text from a PDF.
	 *
	 * @param  pdfContent              The PDF byte content.
	 * @param  strategy                Strategy to read the text from the PDF.
	 * @param  preventDuplicateContent If duplicate content should be ignored.
	 * @return                         The text from the PDF.
	 * @throws IntegrationException    If there is a problem reading the PDF.
	 */
	public static String readPdfText(final byte[] pdfContent, final TextExtractionStrategy strategy,
			final Boolean preventDuplicateContent) throws IntegrationException {
		// PDF reader.
		PdfReader pdfReader = null;
		// Tries to read the PDF.
		try {
			// Creates the reader for the PDF.
			pdfReader = new PdfReader(pdfContent);
			// Reads and returns the PDF text.
			return PdfHelper.readPdfText(pdfReader, strategy, preventDuplicateContent);
		}
		// If the PDF cannot be read.
		catch (final IOException exception) {
			// Throws an PDF read exception.
			throw new IntegrationException(new SimpleMessage("pdf.read.error"), exception);
		}
		// In the end.
		finally {
			// If there is a PDF reader.
			if (pdfReader != null) {
				// Closes the reader.
				pdfReader.close();
			}
		}
	}

}
