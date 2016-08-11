package com.cooksys.serialization.assignment;

import com.cooksys.serialization.assignment.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Creates a {@link Student} object using the given studentContactFile.
     * The studentContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param studentContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return a {@link Student} object built using the {@link Contact} data in the given file
     * @throws JAXBException 
     */
    public static Student readStudent(File studentContactFile, JAXBContext jaxb) throws JAXBException {
        
           	Unmarshaller unmarshaller = jaxb.createUnmarshaller();
           	Student student = new Student();
           	student.setContact((Contact)unmarshaller.unmarshal(studentContactFile));
        	return student;

    }

    /**
     * Creates a list of {@link Student} objects using the given directory of student contact files.
     *
     * @param studentDirectory the directory of student contact files to use
     * @param jaxb the JAXB context to use
     * @return a list of {@link Student} objects built using the contact files in the given directory
     * @throws JAXBException 
     */
    public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) throws JAXBException {
        
    	File[] paths;
      	paths = studentDirectory.listFiles();
      	List<Student> list = new ArrayList<Student>();

    	for (int i =0; i<paths.length; i++){

        	Unmarshaller unmarshaller = jaxb.createUnmarshaller();
        	Student student = new Student();
           	student.setContact((Contact)unmarshaller.unmarshal(paths[i]));
        	list.add(student); 

    	}
    	return list;

    }

    /**
     * Creates an {@link Instructor} object using the given instructorContactFile.
     * The instructorContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param instructorContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return an {@link Instructor} object built using the {@link Contact} data in the given file
     * @throws JAXBException 
     */
    public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) throws JAXBException {
    	
    	Unmarshaller unmarshaller = jaxb.createUnmarshaller();
    	Instructor instructor = new Instructor();
    	instructor.setContact((Contact)unmarshaller.unmarshal(instructorContactFile));
    	return instructor;

    }

    /**
     * Creates a {@link Session} object using the given rootDirectory. A {@link Session}
     * root directory is named after the location of the {@link Session}, and contains a directory named
     * after the start date of the {@link Session}. The start date directory in turn contains a directory named
     * `students`, which contains contact files for the students in the session. The start date directory
     * also contains an instructor contact file named `instructor.xml`.
     *
     * @param rootDirectory the root directory of the session data, named after the session location
     * @param jaxb the JAXB context to use
     * @return a {@link Session} object built from the data in the given directory
     * @throws JAXBException 
     */
    public static Session readSession(File rootDirectory, JAXBContext jaxb) throws JAXBException {
        
    	Session session = new Session();
        
    	String parentDirName = rootDirectory.getParent();
    	
    	//setLocation
    	File file = new File(parentDirName);
       	String[] location = file.list();
       	session.setLocation(location[0]);
       	
    	
        //setDate
       	String[] date = rootDirectory.list();
      	session.setStartDate(date[0]);
       	
       	//setInstructor
      	file = new File(rootDirectory+"/"+date[0]);
    	String[] instructor = file.list();
     	File instructorFile = new File(rootDirectory+"/"+date[0]+"/"+instructor[0]);
    	
    	JAXBContext jaxbInstructor = JAXBContext.newInstance(Instructor.class);
    	session.setInstructor(readInstructor(instructorFile, jaxbInstructor));
    	
    	//setStudent
    	File[] dateDir = file.listFiles();
    	File studentsDir = dateDir[0].isDirectory() ? dateDir[0]: dateDir[1];
    	JAXBContext jaxbStudent = JAXBContext.newInstance(Student.class);
    	session.setStudents(readStudents(studentsDir, jaxbStudent));
    	
    	return session;

    }

    /**
     * Writes a given session to a given XML file
     *
     * @param session the session to write to the given file
     * @param sessionFile the file to which the session is to be written
     * @param jaxb the JAXB context to use
     * @throws JAXBException 
     */
    public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) throws JAXBException {
        // TODO
    	
    	Marshaller jaxMarshaller = jaxb.createMarshaller();
    	
    	jaxMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	
    	jaxMarshaller.marshal(session, sessionFile);
    	jaxMarshaller.marshal(session, System.out);

    	
    }

    /**
     * Main Method Execution Steps:
     * 1. Configure JAXB for the classes in the com.cooksys.serialization.assignment.model package
     * 2. Read a session object from the <project-root>/input/memphis/ directory using the methods defined above
     * 3. Write the session object to the <project-root>/output/session.xml file.
     *
     * JAXB Annotations and Configuration:
     * You will have to add JAXB annotations to the classes in the com.cooksys.serialization.assignment.model package
     *
     * Check the XML files in the <project-root>/input/ directory to determine how to configure the {@link Contact}
     *  JAXB annotations
     *
     * The {@link Session} object should marshal to look like the following:
     *      <session location="..." start-date="...">
     *           <instructor>
     *               <contact>...</contact>
     *           </instructor>
     *           <students>
     *               ...
     *               <student>
     *                   <contact>...</contact>
     *               </student>
     *               ...
     *           </students>
     *      </session>
     * @throws JAXBException 
     */
    public static void main(String[] args) throws JAXBException {
        // TODO
    	
    	
    	
    	
    	
    	JAXBContext jaxbSession = JAXBContext.newInstance(Session.class);
    	
    	File file = new File("input/memphis/");
    	
    	Session result = readSession(file, jaxbSession);
    	
    	File sessionFile = new File("output/session.xml");
    	
    	writeSession(result, sessionFile, jaxbSession);
    	

    }
}
