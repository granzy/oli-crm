import rgbHex from 'rgb-hex';

export default function color(colorVar: string) {
  const colorValue = getComputedStyle(document.body).getPropertyValue(
    `--${colorVar}`
  );

  if (colorValue) {
    if (colorValue.includes('#')) {
      return colorValue;
    }
    if (colorValue.includes('rgb')) {
      return `#${rgbHex(`${colorValue}`)}`;
    }
    return `#${rgbHex(`rgb(${colorValue})`)}`;
  }
  return `#FFFFFF`;
}
