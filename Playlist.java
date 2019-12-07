
/* THIS CODE IS MY OWN WORK,
IT WAS WRITTEN WITHOUT CONSULTING CODE WRITTEN BY OTHER STUDENTS
OR COPIED FROM ONLINE RESOURCES.
LAURA NEFF */



/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using Next or Previous.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Circular Doubly Linked List where each episode has a link to both
/*  the next and the prev episodes in the list.
/*
/*  Additionally, the last Episode is linked to the head of the list (via next),
/*  and the head of the list is linked to that last Episode (via prev). That said,
/*  there is NO special "last" reference keeping track of the last Episode.
/*  But there is a "head" reference that should always refer to the first Episode.
*/

public class Playlist
{
   private Episode head;
   private int size;

   public Playlist()
   {
      head = null;
      size = 0;
   }

   public boolean isEmpty()
   {
     return head == null;
   }

   // Make sure that "size" is updated properly in other methods to
   // always reflect the correct number of episodes in the current playlist
   public int getSize()
   {
     return this.size;
   }

   // Displays the Episodes starting from the head and moving forward
   // Example code and its expected output:
   /*   Playlist pl = new Playlist();
   /*   pl.addLast("PlanetMoney",26.0);
   /*   pl.addLast("HowIBuiltThis",10);
   /*   pl.addLast("RadioLab",25.5);
   /*   pl.displayPlaylistForward();
   /* [BEGIN] (PlanetMoney|26.0MIN) -> (HowIBuiltThis|10.0MIN) -> (RadioLab|25.5MIN) [END]
   */
   public void displayPlaylistForward()
   {
      if (isEmpty()){
         System.out.println("Playlist is empty");
         return;
      }
      String output = "[BEGIN] ";
      Episode current = head;
      while(current.next != head){
       output += current + " -> ";
       current = current.next;
      }
      output += current + " [END]\n";
      System.out.println(output);
   }


   // Displays the Episodes starting from the end and moving backwards
   // Example code and its expected output:
   /*   Playlist pl = new Playlist();
   /*   pl.addLast("PlanetMoney",26.0);
   /*   pl.addLast("HowIBuiltThis",10);
   /*   pl.addLast("RadioLab",25.5);
   /*   pl.displayPlaylistForward();
   /* [END] (RadioLab|25.5MIN) -> (HowIBuiltThis|10.0MIN) -> (PlanetMoney|26.0MIN) [BEGIN]
   */
   public void displayPlaylistBackward()
   {
      if (isEmpty()){
         System.out.println("Playlist is empty");
         return;
      }
      String output = "[END] ";
      Episode current = head.prev;
      while( current != head ){
         output += current + " -> ";
         current = current.prev;
      }
      output += current + " [BEGIN]\n";
      System.out.println(output);
   }

   // Add a new Episode at the beginning of the Playlist
   public void addFirst( String title, double duration )
   {
      Episode newhead = new Episode(title, duration, null, null);
      if (isEmpty()){
         newhead.next = newhead; //link back to itself
         newhead.prev = newhead; //link back to itself
      } else {
         newhead.next = head; //link to the old first node
         newhead.prev = head.prev; //link new head's previous to old head's previous
         head.prev.next = newhead; //the old last element now needs to point to the new head
         head.prev = newhead; //the old head's prev now needs to point to the start of the list since it is first
      }
      head = newhead; //set head to new first element
      size += 1; //add to the size
   }

   // Add a new Episode at the end of the Playlist
   public void addLast(String title, double duration) {
      Episode newlast = new Episode(title, duration, null, null);
      if (isEmpty()) {
         newlast.next = newlast; //link back to self
         newlast.prev = newlast; //link back to self
         head = newlast; //set new head
      } else {
         newlast.next = head; //as last element, the next one is the current first
         newlast.prev = head.prev; //head's prev element is the second to last element in the list which is newlast's last
         head.prev.next = newlast; //second to last element needs to point to the new last element
         head.prev = newlast; //head's previous now points to new end of the list
      }
      size += 1;
   }

   // Add a new Episode at the given index, assuming that index
   // zero corresponds to the first node
   public void add(String title, double duration, int index)
   {
      if (isEmpty() && index==0){
         addLast(title, duration); //we are just adding an element to the start/end of the list
      }
      if (index > size){
         throw new RuntimeException("Index exceeds size of the playlist.");
      }
      Episode current = head;
      for(int i=0;i < index;i++){ //only advance if i is less than index (0 should not advance)
         current = current.next;
      }
      Episode newepi = new Episode(title, duration, current, current.prev);
      current.prev.next = newepi; //change the episode before the current's next to point to new
      current.prev = newepi; //change the current's previous to now point to the new index
      size += 1;
   }

   // Delete the first Episode in the Playlist
   public Episode deleteFirst()
   {
      if (isEmpty()){
         throw new RuntimeException("Playlist is empty");
      }
      Episode current = head;
      head.prev.next = head.next; //link the last element to point to the second element
      head = head.next; //set the head to the second element
      head.prev = current.prev;
      size -= 1;
      return current;
   }

   // Delete the last Episode in the Playlist
   // (There is no special "last" variable in this Playlist;
   // think of alternative ways to find that last Episode)
   public Episode deleteLast()
   {
      if (isEmpty()){
         throw new RuntimeException("Playlist is empty");
      }
      Episode current = head.prev;
      head.prev = head.prev.prev; //change the head's previous element to point to the second to last element
      head.prev.next = head; //change the second to last element to point to head
      size -= 1; //reduce size by 1
      return current;
   }

   // Delete the Episode that has the given "title"
   // You can assume there will be no duplicate titles in any Playlist
   public Episode deleteEpisode(String title)
   {
      Episode current = head;
      while(current.getTitle() != title){
         current = current.next;
      }
      if(current.getTitle()==title){
         current.prev.next = current.next; //change the episode before the current's next to point to after it
         current.prev.next.prev = current.prev;
         if (current == head){
            head = current.next; // if we are changing the head, we also need to handle it
         }
         size -= 1;
      } else{
         throw new RuntimeException("No song with provided title");
      }
      return current;
   }

   // Delete every m-th Episode in the user's circular Playlist,
   // until only one Episode survives. Return the survived Episode.
   public Episode deleteEveryMthEpisode(int m)
   {
      if (isEmpty()){
         throw new RuntimeException("Playlist is empty");
      }
      Episode current = head;
      int cur_idx = 0;
      while(size > 1){
         if(cur_idx % m == 0) {
            current.prev.next = current.next; //change the episode before the current's next to point to after it
            size -= 1;
         }
         current = current.next;
         cur_idx += 1;
      }
      head = current;
      head.next = current;
      head.prev = current;
      return current;
   }

} // End of Playlist class
