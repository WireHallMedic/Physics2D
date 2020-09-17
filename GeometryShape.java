package Physics2D;

public enum GeometryShape
{
   SQUARE,
   ASCENDING_FLOOR, 
   DESCENDING_FLOOR,
   ASCENDING_CEILING,
   DESCENDING_CEILING;
   
   /*
   Ascending Floor:      /
                        /|
                       / |
                      /__|
   
   Decending Floor:   \  
                      |\
                      | \
                      |__\
   
   Ascending Ceil:    |--/
                      |-/
                      |/ 
                      /
   
   Decending Ceil:    \--|
                       \-|
                        \|
                         \
   
   */
}