**Application is used for managing hotel system. <br />**
**Main features  are :**
- Booking a room 
- Changing date of check in/out or canceling booking
- Check in/out 
- Rooms status ( which room is in need to clean and hiding it if employee started to clean room )
- Information abaut delays
- Hiring & Firing of employees
- Sign in/out (not every feature is available to everyone)

**Backend**
- Using Hibernate 
- Using JAX-RS (RESTEasy implementation)
- Using JTA data-source to not manage  transaction myself
- Using Scheduler for operations on DB that need to be done every now and then
- Passwords in DB are encrypted by ARGON2, overriding  method *validatePassword* from *DatabaseServerLoginModule* to check correctness of passwords
- To find set of data I used *Criteria Api* to find all data of type what I need and filter it, but I wonder if better choice would be create for each needs *HQL* queries or use *Criteria Api* with restrictions and let DB engine to filter it
- Using *@RolesAllowed* from *EJB* to restrict access 
- Using Bean Validation
-  Personalizing GSON by excluding some fields from converting and converting date to format yyyy-MM-dd

**Frontend**  
In this project I wanted to focus on backend side therefore for frontend side I had used [Sentra Template](https://templatemo.com/tm-518-sentra) and edited it to my needs.

I  used WildFly17 server with configuration as it is in standalone.xml
