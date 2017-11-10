# This is what I did to install it
(by Mike)

Clone their latest release (into a separate folder outside this repo):

    git clone ssh://git@github.com/PointCloudLibrary/pcl --branch pcl-1.8.1

Follow the build instructions listed on the [pcl github page](https://github.com/PointCloudLibrary/pcl)
- [Linux build instructions](http://www.pointclouds.org/documentation/tutorials/compiling_pcl_posix.php)
- [Windows build instructions](http://www.pointclouds.org/documentation/tutorials/compiling_pcl_windows.php)

An unofficial installationAn guide: [Larry Lisky](https://larrylisky.com/2014/03/03/installing-pcl-on-ubuntu/)

## Depedencies I needed to install
(I was doing this on Debian 8, very similar to ubuntu)

    # build tools
    sudo apt-get install g++ cmake

    # dependencies of pcl
    sudo apt-get install libboost-all-dev libeigen-dev libflann-dev vtk6 libqhull-dev libopenni-dev freeglut3-dev libusb-dev

    # NOTE libboost-all-dev is big (148 mb). Here is a streamlined list of only what pcl needs (48 mbbig):
    sudo apt-get install libboost-system-dev libboost-filesystem-dev libboost-thread-dev libboost-date-time-dev libboost-iostreams-dev

Versions I got:
  - libboost 1.55 (c++ utils)
  - libeigen 3.2.2 (linear algebra)
  - libflann 1.8.4 (??)
  - vtk 6.1.0 (visualization engine)
  - libqhull 2012.1-5 (geometry engine)
  - libopenni (serial connection to the kinect)
  - freeglut3 2.8.1 (opengl gpu rendering)



## Current errors / missing dependencies to resolve

    -- Could NOT find LIBUSB_1 (missing:  LIBUSB_1_LIBRARY LIBUSB_1_INCLUDE_DIR)
    -- checking for module 'metslib'
    --   package 'metslib' not found
    -- VTK not found.  Set the VTK_DIR cmake cache entry to the directory containing VTKConfig.cmake.  This is either the root of the build tree, or PREFIX/lib/vtk for an installation.  For VTK 4.0, this is the location of UseVTK.cmake.  This is either the root of the build tree or PREFIX/include/vtk for an installation.
    -- Could NOT find PCAP (missing:  PCAP_LIBRARIES PCAP_INCLUDE_DIRS)



## The actual build
