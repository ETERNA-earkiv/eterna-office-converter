#!/bin/bash
set -e

echo "[office-documents-converter] Installing dependencies..."

# You can tweak this based on what's already inside keeps/roda image.
# Typical pattern: install unoserver and its dependencies.

# Example (Debian-based image):
apt-get update
apt-get install -y python3-pip
pip3 install --no-cache-dir "unoserver>=1.4.0"

# Optional: if you want local LibreOffice as backup (if UNO container is down):
# apt-get install -y libreoffice
# apt-get clean

echo "[office-documents-converter] Dependencies installed successfully."
