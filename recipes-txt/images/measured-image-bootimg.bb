# bootable image with tboot

LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420 \
    "

IMAGE_INSTALL = "packagegroup-tboot"

# syslinux top level config and labels
AUTO_SYSLINUXMENU = "true"
SYSLINUX_MODULAR = "true"
SYSLINUX_LABELS = "boot"

# syslinux config for individual labels
SYSLINUX_MODULES_boot = "tboot kernel initrd acmsnb acmivb"
SYSLINUX_MODULE_boot-tboot = "/tboot.gz"
SYSLINUX_MODULE_APPEND_boot-tboot = "logging=serial,vga,memory"
SYSLINUX_MODULE_boot-kernel = "/vmlinuz"
SYSLINUX_MODULE_APPEND_boot-kernel = "ramdisk_size=32768 root=/dev/ram0 rw rootimg=rootfs.img rootimgpcr=9 console=tty0 console=ttyS0,115200n8"
SYSLINUX_MODULE_boot-initrd = "/initrd"
SYSLINUX_MODULE_boot-acmsnb = "/acm_snb.bin"
SYSLINUX_MODULE_boot-acmivb = "/acm_ivb.bin"

INITRD_IMAGE = "core-image-tpm-initramfs"
INITRD = "${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}.cpio.gz"

ROOTFS_IMAGE = "core-image-tpm"
ROOTFS = "${DEPLOY_DIR_IMAGE}/${ROOTFS_IMAGE}-${MACHINE}.ext3"

NOHDD = "1"

# be sure the bootimg is built after the initrd and rootfs
do_bootimg[depends] += "${INITRD_IMAGE}:do_rootfs"
do_bootimg[depends] += "${ROOTFS_IMAGE}:do_rootfs"

inherit image
inherit bootimg

# have bootimg populate function grab tboot and ACM
populate_append() {
	install -m 0644 ${DEPLOY_DIR_IMAGE}/tboot-${MACHINE}.gz ${DEST}/tboot.gz
	install -m 0644 ${DEPLOY_DIR_IMAGE}/acm_*.bin ${DEST}/
}
