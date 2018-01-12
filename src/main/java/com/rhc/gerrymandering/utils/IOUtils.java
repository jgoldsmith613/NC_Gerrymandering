package com.rhc.gerrymandering.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.rhc.gerrymandering.domain.ZipCode;
import com.rhc.gerrymandering.domain.ZipCodeDistances;

public class IOUtils {

	public static Collection<ZipCode> readZipInfo(String filename) {

		Collection<ZipCode> zips = new ArrayList<ZipCode>();

		Iterable<CSVRecord> records = null;
		File file = new File(filename);
		try {
			Reader in = new FileReader(file);
			records = CSVFormat.DEFAULT.withHeader().parse(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CSVRecord record : records) {
			zips.add(parseZipInfo(record));
		}

		return zips;

	}

	public static ZipCodeDistances readZipCodeDistances(String filename) {
		ZipCodeDistances zipCodeDistances = new ZipCodeDistances();

		Iterable<CSVRecord> records = null;
		File file = new File(filename);
		try {
			Reader in = new FileReader(file);
			records = CSVFormat.DEFAULT.withHeader().parse(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CSVRecord record : records) {
			zipCodeDistances.addDistancePair(parse(record.get("zip1"), Integer.class), parse(record.get("zip2"), Integer.class), parse(record.get("distance"), Double.class));
		}

		return zipCodeDistances;
	}

	private static ZipCode parseZipInfo(CSVRecord record) {
		ZipCode zipCode = new ZipCode();
		zipCode.setZipCode(parse(record.get("zip_code"), Integer.class));
		zipCode.setLatitude(record.get("lat"));
		zipCode.setLongitude(record.get("long"));
		zipCode.setPopulation(parse(record.get("population"), Integer.class));
		return zipCode;
	}

	private static <T> T parse(String value, Class<T> clazz) {
		if (value.isEmpty()) {
			value = "0";
		}

		if (clazz == Double.class) {
			return clazz.cast(Double.parseDouble(value));
		} else if (clazz == Integer.class) {
			return clazz.cast(Integer.parseInt(value));
		}

		return null;

	}

	public static void save(Collection<ZipCode> zipCodes) throws IOException {

		File file = new File("target/output.csv");

		Appendable out = new OutputStreamWriter(new FileOutputStream(file));

		CSVPrinter printer = CSVFormat.DEFAULT.withHeader(makeHeader()).print(out);

		for (ZipCode visit : zipCodes) {
			printer.printRecord(makeRecord(visit));
		}

		printer.close();

	}

	private static Object[] makeRecord(ZipCode zipCode) {
		String[] record = new String[5];
		record[0] = Integer.toString(zipCode.getZipCode());
		record[1] = zipCode.getLatitude();
		record[2] = zipCode.getLongitude();
		record[3] = Long.toString(zipCode.getPopulation());
		record[4] = Integer.toString(zipCode.getDistrict());
		return record;
	}

	private static String[] makeHeader() {
		return new String[] { "zip_code", "lat", "long", "population", "district" };
	}

}
