# Circuit-Board-app
In this app you can select a circuit board image or any image for that matter and if you click on a pixel in the image you can choose to either show the black and white 
pixels corresponding to the colour of the pixel you clicked on and it will highlight the components of the image. You can also choose to colour the pixel in random colours
or the same colour as the pixel you clicked on. You can also have all the components in the image numbered from largest to smallest depending on the pixel size and you can 
make the blacn and white images look better with the use of noise reduction.
I used a pixel reader, pixel writer and a hash table to connect and find the colour of the pixels.
I used a file chooser to pick an image.
I also used a union find alogorithm to find a root pixel and makes all other pixels around it connect to it to make one whole set. This is how i changes the colours of the components in the images.
