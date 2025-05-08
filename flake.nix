{
  description = "A devShell example";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        overlays = [ ];
        pkgs = import nixpkgs {
          inherit system overlays;
          config.allowUnfree = true;
        };
      in {
        devShells.default = let
          java = pkgs.jetbrains.jdk;
          gradle = pkgs.gradle.override { inherit java; };
          kotlin = pkgs.kotlin.override { jre = java; };
          intellij = pkgs.jetbrains.idea-community;
        in pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            java
            gradle
            kotlin
            intellij
            libGL
            xorg.libX11
            fontconfig
          ];

          shellHook = ''
            export BASE_DIR=$(pwd)
            mkdir -p $BASE_DIR/.share

            if [ -L "$BASE_DIR/.share/java" ]; then
              unlink "$BASE_DIR/.share/java"
            fi
            ln -sf ${java}/lib/openjdk $BASE_DIR/.share/java

            if [ -L "$BASE_DIR/.share/gradle" ]; then
              unlink "$BASE_DIR/.share/gradle" 
            fi
            ln -sf ${gradle}/lib/gradle $BASE_DIR/.share/gradle
            export GRADLE_HOME="$BASE_DIR/.share/gradle"

            export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${
              pkgs.lib.makeLibraryPath [
                kotlin
                pkgs.libGL
                pkgs.xorg.libX11
                pkgs.fontconfig
              ]
            };
          '';
        };
      });
}
