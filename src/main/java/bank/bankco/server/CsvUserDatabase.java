package bank.bankco.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CsvUserDatabase extends UserDatabase {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-16");

    public CsvUserDatabase(Collection<User> users) {
        super(users);
    }

    public CsvUserDatabase(String filename) throws IOException {
        this(filename, DEFAULT_CHARSET);
    }

    public CsvUserDatabase(String filename, Charset charset) 
        throws IOException {
        super(load(filename, charset));
    }

    public void save(String filename) throws IOException {
        save(filename, DEFAULT_CHARSET);
    }

    public void save(String filename, Charset charset) throws IOException {
        try(FileWriter writer = new FileWriter(filename, charset);
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            for(User user : getUsers()) {
                printer.printRecord(user.getType(), user.getAccountNumber(), 
                    user.getEncryptedPassword(), user.getShift());
            }

            printer.flush();
        }
    }

    private static Collection<User> load(String filename, Charset charset) 
            throws IOException {
        try(CSVParser parser = CSVParser.parse(new File(filename), charset, 
            CSVFormat.DEFAULT)) {
            
            ArrayList<User> users = new ArrayList<>();

            for(CSVRecord record : parser) {
                User.Type type = User.Type.valueOf(record.get(0));
                int accountNumber = Integer.parseInt(record.get(1));
                String encryptedPassword = record.get(2);
                int shift = Integer.parseInt(record.get(3));

                User user = new User(type, accountNumber, encryptedPassword,
                    shift);
                users.add(user);

            }

            return users;
        }
    }
}
